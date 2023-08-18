package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.captcha.ArithmeticCaptcha;
import net.renfei.server.core.entity.CaptchaImage;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.RedisService;
import net.renfei.server.core.service.VerificationCodeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.Duration;
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
}
