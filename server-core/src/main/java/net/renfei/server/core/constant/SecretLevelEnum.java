package net.renfei.server.core.constant;

/**
 * 保密等级
 * 绝密不能使用这套系统，所以没有绝密等级
 *
 * @author renfei
 */
public enum SecretLevelEnum {
    /**
     * 非密
     */
    UNCLASSIFIED(0),
    /**
     * 内部
     */
    INTERNAL(1),
    /**
     * 秘密
     */
    SECRET(2),
    /**
     * 机密
     */
    CONFIDENTIAL(3);

    private final int LEVEL;

    SecretLevelEnum(int level) {
        this.LEVEL = level;
    }

    public int getLevel() {
        return LEVEL;
    }

    public static SecretLevelEnum valueOf(int level) {
        switch (level) {
            case 2:
                return SECRET;
            case 3:
                return CONFIDENTIAL;
            default:
                return UNCLASSIFIED;
        }
    }

    /**
     * 是否超越保密等级
     *
     * @param source 当前用户持有的保密等级
     * @param target 需要符合的保密等级
     * @return
     */
    public static boolean outOfSecretLevel(SecretLevelEnum source, SecretLevelEnum target) {
        return target.getLevel() > source.getLevel();
    }
}
