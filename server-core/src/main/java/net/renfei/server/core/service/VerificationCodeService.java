package net.renfei.server.core.service;

import net.renfei.server.core.entity.CaptchaImage;

/**
 * 验证码服务
 *
 * @author renfei
 */
public interface VerificationCodeService {
    /**
     * 生成图形验证码
     *
     * @param width  图片宽度，推荐130
     * @param height 图片高度，推荐48
     * @return
     */
    CaptchaImage generateCaptchaImage(int width, int height);

    /**
     * 验证验证码答案
     *
     * @param id      验证码ID
     * @param answers 答案
     * @return
     */
    boolean verifyCaptchaImage(String id, String answers);
}
