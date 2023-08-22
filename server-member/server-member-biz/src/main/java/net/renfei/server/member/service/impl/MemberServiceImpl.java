package net.renfei.server.member.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sankuai.inf.leaf.IDGen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.payload.request.SettingPasswordRequest;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.core.exception.BusinessException;
import net.renfei.server.core.exception.NeedTotpAuthException;
import net.renfei.server.core.service.*;
import net.renfei.server.core.utils.IpUtils;
import net.renfei.server.core.utils.JwtUtil;
import net.renfei.server.core.utils.StringUtil;
import net.renfei.server.member.api.entity.MemberDetail;
import net.renfei.server.member.api.entity.payload.request.SignUpRequest;
import net.renfei.server.member.repositories.MemberMapper;
import net.renfei.server.member.repositories.entity.Member;
import net.renfei.server.member.repositories.entity.MemberExample;
import net.renfei.server.member.service.MemberService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会员服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends BaseService implements MemberService {
    private final static String MODULE_NAME = "MEMBER";
    /**
     * 最大密码错误次数，超过此阈值将临时锁定
     */
    private final static int MAX_PASSWORD_ERROR_COUNT = 5;
    private final IDGen idGen;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final MemberMapper memberMapper;
    private final EmailService emailService;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final ServerProperties serverProperties;
    private final GoogleAuthenticator googleAuthenticator;
    private final VerificationCodeService verificationCodeService;

    /**
     * 根据用户名查找会员
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public MemberDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.hasLength(username)) {
            MemberExample example = new MemberExample();
            example.createCriteria()
                    .andUsernameEqualTo(username.toLowerCase());
            List<Member> members = memberMapper.selectByExample(example);
            if (!members.isEmpty()) {
                return this.convert(members.get(0));
            }
        }
        throw new UsernameNotFoundException("会员不存在");
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        MemberDetail memberDetail;
        try {
            memberDetail = this.loadUserByUsername(signInRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new BusinessException("用户名或密码错误，请重试。");
        }
        if (StringUtils.hasLength(memberDetail.getTotp())
                && signInRequest.getTotp() == null) {
            throw new NeedTotpAuthException("需要TOTP两步认证，请重试。");
        }
        if (serverProperties.getEnableCustomEncryption()) {
            // 将密码解密
            signInRequest.setPassword(
                    securityService.decryptAesByKeyId(signInRequest.getPassword(), signInRequest.getKeyId()));
        }
        if (!memberDetail.getEnabled()) {
            // TODO 激活邮件中的 callBackLink 需要确认
            verificationCodeService.sendVerifyAccountEmail(memberDetail.getEmail(), memberDetail.getUsername(), "signup", "");
            throw new BusinessException("您的账户未被激活，我们向您注册的邮箱发送了一封激活邮件，请查收。");
        }
        if (memberDetail.getLocked()) {
            securityService.insertAuditLog(LogLevelEnum.WARN,
                    MODULE_NAME, "尝试使用被锁定的账户登录",
                    "账户[" + memberDetail.getUsername() + "]因被锁定拒绝登入系统。", null);
            throw new BusinessException("您的账户已经被锁定，请联系管理员解锁您的账户。");
        }
        if (memberDetail.getLockTime() != null
                && new Date().compareTo(memberDetail.getLockTime()) < 0) {
            securityService.insertAuditLog(LogLevelEnum.INFO,
                    MODULE_NAME, "尝试使用被临时锁定的账户登录",
                    "账户[" + memberDetail.getUsername() + "]因被临时锁定被拒绝登入系统。", null);
            throw new BusinessException("您的账户被临时锁定，随时间可自动解锁，请稍后再试；请勿连续尝试错误密码。");
        }
        if (!passwordEncoder.matches(signInRequest.getPassword(), memberDetail.getPassword())) {
            // 记录警告事件
            this.recordPasswordErrorEvent(memberDetail);
            throw new BusinessException("用户名或密码错误，请重试。");
        }
        if (StringUtils.hasLength(memberDetail.getTotp())) {
            // 将TOTP置入Redis，只能使用一次
            String redisKey = RedisService.AUTH_TOTP_KEY + "member:" + memberDetail.getUsername();
            if (redisService.contain(redisKey)
                    && memberDetail.getTotp().equals(redisService.getValue(redisKey))) {
                // 缓存中已经存在，说明使用过了
                throw new BusinessException("TOTP两步认证失败，注意同一个TOTP验证码只能使用一次，请重试。");
            } else {
                // 没有使用过，放入缓存
                redisService.setValue(redisKey, memberDetail.getTotp(), Duration.ofHours(1));
            }
            if (!googleAuthenticator.authorize(memberDetail.getTotp(), signInRequest.getTotp())) {
                throw new BusinessException("TOTP两步认证失败，注意同一个TOTP验证码只能使用一次，请重试。");
            }
        }
        String token = jwtUtil.generateToken(memberDetail, "member");
        // 放入 Redis
        redisService.setValue(RedisService.AUTH_TOKEN_KEY
                + "member:" + memberDetail.getUsername(), token, Duration.ofHours(8));
        securityService.insertAuditLog(LogLevelEnum.INFO,
                MODULE_NAME, "用户登入系统",
                "账户[" + memberDetail.getUsername() + "]成功登入系统。", null);
        log.info("用户：{} 成功登入系统。", signInRequest.getUsername());
        return SignInResponse.builder()
                .accessToken(token)
                .build();
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        Assert.notNull(signUpRequest, "请求体不能为空");
        Assert.hasLength(signUpRequest.getUsername(), "用户名不能为空");
        Assert.hasLength(signUpRequest.getEmail(), "电子邮箱不能为空");
        Assert.hasLength(signUpRequest.getPassword(), "密码不能为空");
        Assert.hasLength(signUpRequest.getKeyId(), "密码需要加密传输");
        signUpRequest.setUsername(signUpRequest.getUsername().toLowerCase());
        signUpRequest.setEmail(signUpRequest.getEmail().toLowerCase());
        Assert.isTrue(StringUtil.isEmail(signUpRequest.getEmail()), "电子邮箱格式不正确");
        MemberExample example = new MemberExample();
        example.createCriteria().andUsernameEqualTo(signUpRequest.getUsername().toLowerCase());
        Assert.isTrue(memberMapper.selectByExample(example).isEmpty(), "用户名已经被占用，请更换一个重试。");
        example = new MemberExample();
        example.createCriteria().andEmailEqualTo(signUpRequest.getEmail().toLowerCase());
        Assert.isTrue(memberMapper.selectByExample(example).isEmpty(), "电子邮箱已经被占用，请更换一个重试。");
        try {
            // 对密码进行解密
            signUpRequest.setPassword(
                    securityService.decryptAesByKeyId(signUpRequest.getPassword(),
                            signUpRequest.getKeyId()));
        } catch (Exception e) {
            log.error("密码解密失败", e);
            throw new BusinessException("密码解密失败，请联系管理员");
        }
        Assert.isTrue(securityService.weakPasswordCheck(signUpRequest.getPassword()),
                "该密码为弱密码，请更换一个强密码，重试");
        Member member = new Member();
        member.setId(idGen.get("").getId());
        member.setPassword(signUpRequest.getPassword());
        member.setEmail(signUpRequest.getEmail());
        member.setCreateTime(new Date());
        member.setUpdateTime(new Date());
        member.setLocked(false);
        member.setEnabled(false);
        member.setRegistrationIp(IpUtils.getIpAddress(getCurrentRequest()));
        memberMapper.insertSelective(member);
        // TODO 激活邮件中的 callBackLink 需要确认
        verificationCodeService.sendVerifyAccountEmail(signUpRequest.getEmail(), signUpRequest.getUsername(), "signup", "");
    }

    @Override
    public void activationMemberAccount(String email, String code, String type) {
        if (!verificationCodeService.verifyAccountEmail(email, type, code)) {
            throw new BusinessException("激活失败，验证码不正确或已过期");
        }
    }

    @Override
    public ListData<MemberDetail> queryMemberUser(String username, String email,
                                                  String mobile, String name, int pages, int rows) {
        MemberExample example = new MemberExample();
        example.setOrderByClause("createTime DESC");
        MemberExample.Criteria criteria = example.createCriteria();
        if (StringUtils.hasLength(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (StringUtils.hasLength(email)) {
            criteria.andEmailLike("%" + email + "%");
        }
        if (StringUtils.hasLength(mobile)) {
            criteria.andMobileLike("%" + mobile + "%");
        }
        if (StringUtils.hasLength(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        try (Page<Member> page = PageHelper.startPage(pages, rows)) {
            memberMapper.selectByExample(example);
            List<MemberDetail> memberDetails = new ArrayList<>(page.size());
            page.forEach(member -> memberDetails.add(this.convert(member)));
            return new ListData<>(page, memberDetails);
        }
    }

    @Override
    public void createMemberUser(MemberDetail memberDetail) {
        Assert.notNull(memberDetail, "请求体不能为空");
        Assert.hasLength(memberDetail.getUsername(), "用户名不能为空");
        MemberExample example = new MemberExample();
        example.createCriteria().andUsernameEqualTo(memberDetail.getUsername().toLowerCase());
        Assert.isTrue(memberMapper.selectByExample(example).isEmpty(), "用户名已经被占用，请更换一个重试。");
        if (StringUtils.hasLength(memberDetail.getEmail())) {
            example = new MemberExample();
            example.createCriteria().andEmailEqualTo(memberDetail.getEmail().toLowerCase());
            Assert.isTrue(memberMapper.selectByExample(example).isEmpty(), "电子邮箱已经被占用，请更换一个重试。");
        }
        if (StringUtils.hasLength(memberDetail.getMobile())) {
            example = new MemberExample();
            example.createCriteria().andMobileEqualTo(memberDetail.getMobile().toLowerCase());
            Assert.isTrue(memberMapper.selectByExample(example).isEmpty(), "手机号已经被占用，请更换一个重试。");
        }
        Member member = this.convert(memberDetail);
        member.setId(null);
        member.setPassword(serverProperties.getDefaultPassword());
        member.setTotp(null);
        member.setCreateTime(new Date());
        member.setLocked(false);
        member.setEnabled(false);
        member.setRegistrationIp(IpUtils.getIpAddress(getCurrentRequest()));
        member.setUpdateTime(new Date());
        memberMapper.insertSelective(member);
    }

    @Override
    public void updateMemberUser(String username, MemberDetail memberDetail) {
        Assert.hasLength(username, "用户名不能为空");
        Assert.notNull(memberDetail, "请求体不能为空");
        MemberExample example = new MemberExample();
        example.createCriteria()
                .andUsernameEqualTo(username);
        List<Member> members = memberMapper.selectByExample(example);
        Assert.isTrue(!members.isEmpty(), "根据用户名未能找到用户");
        Member member = members.get(0);
        if (StringUtils.hasLength(memberDetail.getEmail())
                && !memberDetail.getEmail().equals(member.getEmail())) {
            example = new MemberExample();
            example.createCriteria().andEmailEqualTo(memberDetail.getEmail().toLowerCase());
            Assert.isTrue(memberMapper.selectByExample(example).isEmpty(), "电子邮箱已经被占用，请更换一个重试。");
        }
        if (StringUtils.hasLength(memberDetail.getMobile())
                && !memberDetail.getMobile().equals(member.getMobile())) {
            example = new MemberExample();
            example.createCriteria().andMobileEqualTo(memberDetail.getMobile().toLowerCase());
            Assert.isTrue(memberMapper.selectByExample(example).isEmpty(), "手机号已经被占用，请更换一个重试。");
        }
        member.setName(memberDetail.getName());
        member.setGender(memberDetail.getGender());
        member.setDescription(memberDetail.getDescription());
        member.setBirthDay(memberDetail.getBirthDay());
        member.setUpdateTime(new Date());
        memberMapper.updateByPrimaryKey(member);
    }

    @Override
    public void settingMemberPassword(String username, SettingPasswordRequest settingPasswordRequest) {
        Assert.hasLength(username, "用户名不能为空");
        Assert.notNull(settingPasswordRequest, "请求体不能为空");
        Assert.hasLength(settingPasswordRequest.getPassword(), "密码不能为空");
        MemberExample example = new MemberExample();
        example.createCriteria()
                .andUsernameEqualTo(username);
        List<Member> members = memberMapper.selectByExample(example);
        Assert.isTrue(!members.isEmpty(), "根据用户名未能找到用户");
        if (StringUtils.hasLength(settingPasswordRequest.getKeyId())) {
            settingPasswordRequest.setPassword(
                    securityService.decryptAesByKeyId(settingPasswordRequest.getPassword(),
                            settingPasswordRequest.getKeyId()));
        }
        Assert.isTrue(securityService.weakPasswordCheck(settingPasswordRequest.getPassword()),
                "该密码为弱密码，请更换一个强密码，重试");
        Member member = members.get(0);
        member.setPassword(settingPasswordRequest.getPassword());
        member.setErrorCount(0);
        member.setLockTime(DateUtils.addSeconds(new Date(), -1));
        member.setPasswordUpdateTime(new Date());
        member.setUpdateTime(new Date());
        memberMapper.updateByPrimaryKeySelective(member);
        securityService.cleanTokenCache(username);
    }

    @Override
    public void enableMember(String username, boolean enable) {
        Assert.hasLength(username, "用户名不能为空");
        MemberExample example = new MemberExample();
        example.createCriteria()
                .andUsernameEqualTo(username);
        List<Member> sysUsers = memberMapper.selectByExample(example);
        Assert.isTrue(!sysUsers.isEmpty(), "根据用户名未能找到用户");
        Member member = sysUsers.get(0);
        member.setEnabled(enable);
        member.setUpdateTime(new Date());
        memberMapper.updateByPrimaryKeySelective(member);
        if (!enable) {
            securityService.cleanTokenCache(username);
        }
    }

    @Override
    public void lockedMember(String username, boolean locked) {
        Assert.hasLength(username, "用户名不能为空");
        MemberExample example = new MemberExample();
        example.createCriteria()
                .andUsernameEqualTo(username);
        List<Member> sysUsers = memberMapper.selectByExample(example);
        Assert.isTrue(!sysUsers.isEmpty(), "根据用户名未能找到用户");
        Member member = sysUsers.get(0);
        member.setLocked(locked);
        member.setErrorCount(0);
        member.setLockTime(null);
        member.setUpdateTime(new Date());
        memberMapper.updateByPrimaryKey(member);
        if (locked) {
            securityService.cleanTokenCache(username);
        }
    }

    @Override
    public void forgotUsername(String email) {
        Assert.hasLength(email, "邮箱不能为空");
        email = email.toLowerCase();
        MemberExample example = new MemberExample();
        if (StringUtil.isEmail(email)) {
            example.createCriteria()
                    .andEmailEqualTo(email);
            List<Member> sysUsers = memberMapper.selectByExample(example);
            if (!sysUsers.isEmpty()) {
                Member member = sysUsers.get(0);
                if (StringUtils.hasLength(member.getEmail())) {
                    List<String> contents = new ArrayList<>();
                    contents.add("尊敬的 " + member.getUsername() + " :");
                    contents.add("这封信是由[RENFEI.NET]系统自动发送的。");
                    contents.add("您收到这封邮件，是由于您在[RENFEI.NET]进行了找回用户名操作。如果您并没有访问过[RENFEI.NET]或没有进行上述操作，请忽略这封邮件。您不需要退订或进行其他进一步的操作。");
                    contents.add("----------------------------------------------------------------------");
                    contents.add("用户名找回说明");
                    contents.add("----------------------------------------------------------------------");
                    contents.add("");
                    contents.add("您在[RENFEI.NET]的用户名是：");
                    contents.add("");
                    contents.add("<span style=\"color:red;font-size:18px;font-weight:800;\">" + member.getUsername() + "</span>");
                    contents.add("");
                    contents.add("----");
                    contents.add("感谢您的访问，祝您使用愉快！");
                    emailService.send(email, member.getUsername(), "找回您在[RENFEI.NET]的账户用户名", contents);
                }
            }
        }
    }

    @Override
    public void forgotPassword(String usernameOrEmail) {
        Assert.hasLength(usernameOrEmail, "用户名或邮箱不能为空");
        usernameOrEmail = usernameOrEmail.toLowerCase();
        MemberExample example = new MemberExample();
        if (StringUtil.isEmail(usernameOrEmail)) {
            example.createCriteria()
                    .andEmailEqualTo(usernameOrEmail);
        } else {
            example.createCriteria()
                    .andUsernameEqualTo(usernameOrEmail);
        }
        List<Member> sysUsers = memberMapper.selectByExample(example);
        if (!sysUsers.isEmpty()) {
            Member member = sysUsers.get(0);
            if (member.getEnabled()
                    && StringUtils.hasLength(member.getEmail())) {
                // TODO 回调地址
                verificationCodeService.sendForgotPasswordEmail(member.getEmail(), member.getUsername(), "forgotpassword", "");
            }
        }
    }

    public void forgotPassword(String usernameOrEmail, String code, SettingPasswordRequest settingPasswordRequest) {
        Assert.hasLength(usernameOrEmail, "用户名或邮箱不能为空");
        Assert.hasLength(code, "验证码不能为空");
        Assert.notNull(settingPasswordRequest, "请求体不能为空");
        Assert.hasLength(settingPasswordRequest.getPassword(), "密码不能为空");
        usernameOrEmail = usernameOrEmail.toLowerCase();
        MemberExample example = new MemberExample();
        if (StringUtil.isEmail(usernameOrEmail)) {
            example.createCriteria()
                    .andEmailEqualTo(usernameOrEmail);
        } else {
            example.createCriteria()
                    .andUsernameEqualTo(usernameOrEmail);
        }
        List<Member> sysUsers = memberMapper.selectByExample(example);
        if (!sysUsers.isEmpty()) {
            Member member = sysUsers.get(0);
            if (member.getEnabled()) {
                if (StringUtils.hasLength(settingPasswordRequest.getKeyId())) {
                    settingPasswordRequest.setPassword(
                            securityService.decryptAesByKeyId(settingPasswordRequest.getPassword(),
                                    settingPasswordRequest.getKeyId()));
                }
                Assert.isTrue(securityService.weakPasswordCheck(settingPasswordRequest.getPassword()),
                        "该密码为弱密码，请更换一个强密码，重试");
                if (verificationCodeService.verifyForgotPasswordEmail(member.getEmail(), "forgotpassword", code)) {
                    member.setPassword(settingPasswordRequest.getPassword());
                    member.setErrorCount(0);
                    member.setLockTime(DateUtils.addSeconds(new Date(), -1));
                    member.setPasswordUpdateTime(new Date());
                    member.setUpdateTime(new Date());
                    memberMapper.updateByPrimaryKeySelective(member);
                    securityService.cleanTokenCache(member.getUsername());
                } else {
                    throw new BusinessException("验证码错误或已经过期，请重新申请验证码");
                }
            } else {
                throw new BusinessException("您的账户未启用，请先执行登录操作，触发账户激活流程");
            }
        }
    }

    /**
     * 记录密码错误事件
     *
     * @param memberDetail 用户
     */
    private void recordPasswordErrorEvent(MemberDetail memberDetail) {
        MemberExample example = new MemberExample();
        example.createCriteria()
                .andUsernameEqualTo(memberDetail.getUsername().toLowerCase());
        List<Member> members = memberMapper.selectByExample(example);
        if (!members.isEmpty()) {
            Member member = members.get(0);
            if (member.getErrorCount() == null || member.getErrorCount() < 0) {
                member.setErrorCount(0);
            }
            member.setErrorCount(member.getErrorCount() + 1);
            if (member.getErrorCount() > MAX_PASSWORD_ERROR_COUNT) {
                // 增加临时锁定时间，错误N次，锁定N*5分钟，防止密码爆破
                member.setLockTime(DateUtils.addMinutes(new Date(), (member.getErrorCount() - MAX_PASSWORD_ERROR_COUNT) * 5));
            }
            memberMapper.updateByPrimaryKey(member);
            securityService.insertAuditLog(LogLevelEnum.WARN,
                    MODULE_NAME, "尝试使用错误的密码登录系统",
                    "账户[" + memberDetail.getUsername() + "]尝试使用错误的密码登录系统被拒绝。", null);
        }
    }

    private MemberDetail convert(Member member) {
        if (member == null) {
            return null;
        }
        MemberDetail memberDetail = new MemberDetail();
        BeanUtils.copyProperties(member, memberDetail);
        memberDetail.setId(member.getId().toString());
        memberDetail.setRegistrationDate(member.getCreateTime());
        return memberDetail;
    }

    private Member convert(MemberDetail memberDetail) {
        if (memberDetail == null) {
            return null;
        }
        Member member = new Member();
        BeanUtils.copyProperties(memberDetail, member);
        member.setId(Long.valueOf(memberDetail.getId()));
        member.setCreateTime(memberDetail.getRegistrationDate());
        return member;
    }
}
