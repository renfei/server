package net.renfei.server.core.constant;

/**
 * 文本扫描类型
 *
 * @author renfei
 */
public enum TextModerationTypeEnum {
    /**
     * 用户昵称检测
     */
    NICKNAME_DETECTION("nickname_detection"),
    /**
     * 私聊互动内容检测
     */
    CHAT_DETECTION("chat_detection"),
    /**
     * 公聊评论内容检测
     */
    COMMENT_DETECTION("comment_detection"),
    /**
     * AIGC文字检测
     */
    AI_ART_DETECTION("ai_art_detection"),
    /**
     * 广告法合规检测
     */
    AD_COMPLIANCE_DETECTION("ad_compliance_detection"),
    /**
     * PGC教学物料检测
     */
    PGC_DETECTION("pgc_detection");
    private final String value;

    TextModerationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
