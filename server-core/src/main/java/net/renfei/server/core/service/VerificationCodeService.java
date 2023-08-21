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

    /**
     * 发送验证账户的Email
     *
     * @param email        电子邮箱
     * @param username     用户称呼
     * @param type         类型：新增？修改？
     * @param callBackLink 回调地址，邮件中的链接
     */
    void sendVerifyAccountEmail(String email, String username, String type, String callBackLink);

    /**
     * 验证邮箱验证码
     *
     * @param email 电子邮箱
     * @param type  类型：新增？修改？
     * @param code  验证码
     * @return
     */
    boolean verifyAccountEmail(String email, String type, String code);
}
