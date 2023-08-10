package net.renfei.server.core.constant;

/**
 * 图片扫描类型
 *
 * @author renfei
 */
public enum ImageModerationTypeEnum {
    /**
     * 通用基线检测
     */
    BASELINE_CHECK("baselineCheck"),
    /**
     * 通用基线检测_专业版
     */
    BASELINE_CHECK_PRO("baselineCheck_pro"),
    /**
     * 通用基线检测_海外版
     */
    BASELINE_CHECK_CB("baselineCheck_cb"),
    /**
     * 内容治理检测
     */
    TONALITY_IMPROVE("tonalityImprove"),
    /**
     * AIGC图片检测
     */
    AIGC_CHECK("aigcCheck"),
    /**
     * 头像图片检测
     */
    PROFILE_PHOTO_CHECK("profilePhotoCheck"),
    /**
     * 营销素材检测
     */
    ADVERTISING_CHECK("advertisingCheck"),
    /**
     * 视频\直播截图检测
     */
    LIVE_STREAM_CHECK("liveStreamCheck");
    private final String value;

    ImageModerationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
