package net.renfei.server.core.service;

import net.renfei.server.core.constant.ImageModerationTypeEnum;
import net.renfei.server.core.constant.TextModerationTypeEnum;
import net.renfei.server.core.entity.ImageModerationResult;
import net.renfei.server.core.entity.TextModerationResult;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author renfei
 */
public class ModerationServiceTests extends ApplicationTest {
    @Autowired
    private ModerationService moderationService;

    @Test
    public void textModeration() {
        TextModerationResult moderationResult = moderationService.textModeration("毛泽东语录", TextModerationTypeEnum.COMMENT_DETECTION);
        System.out.println(moderationResult.toString());
        moderationResult = moderationService.textModeration("今天是个好日子", TextModerationTypeEnum.COMMENT_DETECTION);
        System.out.println(moderationResult.toString());
    }

    @Test
    public void imageModeration() {
        ImageModerationResult moderationResult = moderationService.imageModeration(
                "https://p2.cri.cn/M00/2B/C2/rBABC2CoLuqAfceIAAAAAAAAAAA281.4400x2866.jpg",
                "abc", ImageModerationTypeEnum.BASELINE_CHECK);
        System.out.println(moderationResult.toString());
//        moderationResult = moderationService.imageModeration(
//                "https://up.enterdesk.com/edpic_source/0d/fd/56/0dfd56f3d744002ed3718e37860352f9.jpeg",
//                "def", ImageModerationTypeEnum.BASELINE_CHECK);
//        System.out.println(moderationResult.toString());
    }
}
