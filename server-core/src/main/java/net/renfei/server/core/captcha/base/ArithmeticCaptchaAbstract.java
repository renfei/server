package net.renfei.server.core.captcha.base;

/**
 * 算术验证码抽象类
 *
 * @author 王帆
 */
public abstract class ArithmeticCaptchaAbstract extends Captcha {
    /**
     * 计算公式
     */
    private String arithmeticString;

    public ArithmeticCaptchaAbstract() {
        setLen(2);
    }

    /**
     * 生成随机验证码
     */
    @Override
    protected void alphas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(num(10));
            if (i < len - 1) {
                int type = num(1, 4);
                if (type == 1) {
                    sb.append("+");
                } else if (type == 2) {
                    sb.append("-");
                } else if (type == 3) {
                    sb.append("x");
                }
            }
        }

        int result = (int) Calculator.conversion(sb.toString().replaceAll("x", "*"));
        this.chars = String.valueOf(result);

        sb.append("=?");
        arithmeticString = sb.toString();
    }

    public String getArithmeticString() {
        checkAlpha();
        return arithmeticString;
    }

    public void setArithmeticString(String arithmeticString) {
        this.arithmeticString = arithmeticString;
    }
}
