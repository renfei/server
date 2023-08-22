package net.renfei.server.member.service;

import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.payload.request.SettingPasswordRequest;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.member.api.entity.MemberDetail;
import net.renfei.server.member.api.entity.payload.request.SignUpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 会员服务
 *
 * @author renfei
 */
public interface MemberService extends UserDetailsService {
    /**
     * 根据用户名查找用户
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    MemberDetail loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 会员登录
     *
     * @param signInRequest 登录请求对象
     * @return
     */
    SignInResponse signIn(SignInRequest signInRequest);

    /**
     * 注册会员账户
     *
     * @param signUpRequest 注册请求对象
     */
    void signUp(SignUpRequest signUpRequest);

    /**
     * 激活账户
     *
     * @param email 邮箱
     * @param code  验证码
     * @param type  类型
     */
    void activationMemberAccount(String email, String code, String type);

    /**
     * 查询会员列表
     *
     * @param username 用户名
     * @param email    电子邮箱
     * @param mobile   手机号
     * @param name     姓名
     * @param pages    页码
     * @param rows     每页容量
     * @return
     */
    ListData<MemberDetail> queryMemberUser(String username, String email,
                                           String mobile, String name, int pages, int rows);

    /**
     * 创建会员账户
     *
     * @param memberDetail 会员详情
     */
    void createMemberUser(MemberDetail memberDetail);

    /**
     * 更新会员资料
     *
     * @param username     用户名
     * @param memberDetail 会员详情
     */
    void updateMemberUser(String username, MemberDetail memberDetail);

    /**
     * 重设会员账户密码
     *
     * @param username               用户名
     * @param settingPasswordRequest 重设密码
     */
    void settingMemberPassword(String username, SettingPasswordRequest settingPasswordRequest);

    /**
     * 启用或禁用会员账户
     *
     * @param username 用户名
     * @param enable   是否启用
     */
    void enableMember(String username, boolean enable);

    /**
     * 锁定或解锁会员
     *
     * @param username 用户名
     * @param locked   是否锁定
     */
    void lockedMember(String username, boolean locked);

    /**
     * 忘记用户名，找回用户名
     *
     * @param email 邮箱
     */
    void forgotUsername(String email);

    /**
     * 忘记密码
     *
     * @param usernameOrEmail 用户名或密码
     */
    void forgotPassword(String usernameOrEmail);

    /**
     * 忘记密码重置密码
     *
     * @param usernameOrEmail        用户名或密码
     * @param code                   验证码
     * @param settingPasswordRequest 新密码
     */
    void forgotPassword(String usernameOrEmail, String code, SettingPasswordRequest settingPasswordRequest);
}
