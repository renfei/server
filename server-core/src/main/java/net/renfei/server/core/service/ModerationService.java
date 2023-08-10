package net.renfei.server.core.service;

import net.renfei.server.core.constant.ImageModerationTypeEnum;
import net.renfei.server.core.constant.TextModerationTypeEnum;
import net.renfei.server.core.entity.ImageModerationResult;
import net.renfei.server.core.entity.TextModerationResult;

/**
 * 内容安全服务（中国特有）
 *
 * @author renfei
 */
public interface ModerationService {
    /**
     * 文本扫描
     *
     * @param value          待扫描内容
     * @param moderationType 扫描类型
     * @return
     */
    TextModerationResult textModeration(String value, TextModerationTypeEnum moderationType);

    /**
     * 图片扫描
     *
     * @param imageUrl       待扫描图片连接
     * @param dataId         数据ID
     * @param moderationType 扫描类型
     * @return
     */
    ImageModerationResult imageModeration(String imageUrl, String dataId, ImageModerationTypeEnum moderationType);
}
