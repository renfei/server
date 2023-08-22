package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.captcha.ArithmeticCaptcha;
import net.renfei.server.core.entity.CaptchaImage;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.EmailService;
import net.renfei.server.core.service.RedisService;
import net.renfei.server.core.service.VerificationCodeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 验证码服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl extends BaseService implements VerificationCodeService {
    private final static String REDIS_CAPTCHA_KEY = RedisService.REDIS_ROOT_KEY + "captcha:";
    private final RedisService redisService;
    private final EmailService emailService;

    @Override
    public CaptchaImage generateCaptchaImage(int width, int height) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(width, height);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String redisKey = REDIS_CAPTCHA_KEY + uuid;
        redisService.setValue(redisKey, captcha.text(), Duration.ofMinutes(5));
        return CaptchaImage.builder()
                .id(uuid)
                .base64(captcha.toBase64())
                .build();
    }

    @Override
    public boolean verifyCaptchaImage(String id, String answers) {
        if (StringUtils.hasLength(id) && StringUtils.hasLength(answers)) {
            String redisKey = REDIS_CAPTCHA_KEY + id;
            if (redisService.contain(redisKey)) {
                Serializable value = redisService.getValue(redisKey);
                if (answers.equals(value)) {
                    redisService.delete(redisKey);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void sendVerifyAccountEmail(String email, String username, String type, String callBackLink) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String redisKey = RedisService.REDIS_ROOT_KEY + "verify:email:" + type + ":" + email;
        List<String> contents = new ArrayList<>();
        String userName = "先生/女士";
        if (StringUtils.hasLength(username)) {
            userName = username;
        }
        contents.add("尊敬的 " + userName + " :");
        contents.add("这封信是由[RENFEI.NET]系统自动发送的。");
        contents.add("您收到这封邮件，是由于在[RENFEI.NET]创建了新的账户或修改 Email 使用了这个邮箱地址，又或者您正在进行敏感操作需要验证您的身份。如果您并没有访问过[RENFEI.NET]或没有进行上述操作，请忽略这封邮件。您不需要退订或进行其他进一步的操作。");
        contents.add("----------------------------------------------------------------------");
        contents.add("帐号验证说明");
        contents.add("----------------------------------------------------------------------");
        contents.add("如果您是[RENFEI.NET]的新用户，或在修改您的注册 Email 时使用了此邮箱地址，又或者您正在进行敏感操作，我们需要对您的操作有效性进行验证以避免非本人操作或服务滥用。");
        if (StringUtils.hasLength(callBackLink)) {
            contents.add("您只需点击下面的链接即可：");
            contents.add("<a href=\"" + callBackLink + "?code=" + uuid + "\">" + callBackLink + "?code=" + uuid + "</a>");
            contents.add("(如果上面不是链接形式，请将该地址手工粘贴到浏览器地址栏再访问)");
        } else {
            contents.add("您的验证码是：");
            contents.add("<span style=\"color:red;font-size:18px;font-weight:800;\">" + uuid + "</span>");
        }
        contents.add("验证码有效期为 2小时。");
        contents.add("----");
        contents.add("感谢您的访问，祝您使用愉快！");
        emailService.send(email, userName, "验证邮件：验证您在[RENFEI.NET]的账户", contents);
        redisService.setValue(redisKey, uuid, Duration.ofHours(2));
    }

    @Override
    public boolean verifyAccountEmail(String email, String type, String code) {
        if (StringUtils.hasLength(email) && StringUtils.hasLength(type) && StringUtils.hasLength(code)) {
            String redisKey = RedisService.REDIS_ROOT_KEY + "email:verify:" + type + ":" + email;
            if (redisService.contain(redisKey)) {
                Serializable value = redisService.getValue(redisKey);
                if (code.equals(value)) {
                    redisService.delete(redisKey);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void sendForgotPasswordEmail(String email, String username, String type, String callBackLink) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String redisKey = RedisService.REDIS_ROOT_KEY + "email:" + type + ":" + email;
        List<String> contents = new ArrayList<>();
        String userName = "先生/女士";
        if (StringUtils.hasLength(username)) {
            userName = username;
        }
        contents.add("尊敬的 " + userName + " :");
        contents.add("这封信是由[RENFEI.NET]系统自动发送的。");
        contents.add("您收到这封邮件，是由于您在[RENFEI.NET]进行了找回密码操作。如果您并没有访问过[RENFEI.NET]或没有进行上述操作，请忽略这封邮件。您不需要退订或进行其他进一步的操作。");
        contents.add("----------------------------------------------------------------------");
        contents.add("忘记密码帐号找回说明");
        contents.add("----------------------------------------------------------------------");
        contents.add("如果是您本人在[RENFEI.NET]操作的密码找回申请，我们需要对您的找回操作进行验证以避免非本人操作或服务滥用。");
        if (StringUtils.hasLength(callBackLink)) {
            contents.add("您只需点击下面的链接即可：");
            contents.add("<a href=\"" + callBackLink + "?code=" + uuid + "\">" + callBackLink + "?code=" + uuid + "</a>");
            contents.add("(如果上面不是链接形式，请将该地址手工粘贴到浏览器地址栏再访问)");
        } else {
            contents.add("您的验证码是：");
            contents.add("<span style=\"color:red;font-size:18px;font-weight:800;\">" + uuid + "</span>");
        }
        contents.add("验证码有效期为 2小时。");
        contents.add("----");
        contents.add("感谢您的访问，祝您使用愉快！");
        emailService.send(email, userName, "验证邮件：忘记密码找回您在[RENFEI.NET]的账户", contents);
        redisService.setValue(redisKey, uuid, Duration.ofHours(2));
    }

    @Override
    public boolean verifyForgotPasswordEmail(String email, String type, String code) {
        if (StringUtils.hasLength(email) && StringUtils.hasLength(type) && StringUtils.hasLength(code)) {
            String redisKey = RedisService.REDIS_ROOT_KEY + "email:forgotpassword:" + type + ":" + email;
            if (redisService.contain(redisKey)) {
                Serializable value = redisService.getValue(redisKey);
                if (code.equals(value)) {
                    redisService.delete(redisKey);
                    return true;
                }
            }
        }
        return false;
    }
}
