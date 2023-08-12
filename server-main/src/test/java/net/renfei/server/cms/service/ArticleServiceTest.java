package net.renfei.server.cms.service;

import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author renfei
 */
public class ArticleServiceTest extends ApplicationTest {
    @Autowired
    private ArticleService articleService;

    @Test
    public void getWordCount() {
        String html = "<img src=\"https://cdn.renfei.net/upload/2022/8010d01a83264822b91b279059615e86.webp\" alt=\"IT资讯网站 cnBeta.com 网站被关停域名已经被 clientHold\" /><p style=\"text-indent: 2em;\">10月25日，很多自媒体都在说IT资讯网站 cnBeta.com 备案被注销，我看了下，国内没有做解析，国外解析到cloudflare，我跟他们一样猜测是注销个人备案，然后换企业备案。</p><p style=\"text-indent: 2em;\">但今天再看，发现国外的解析也没了，经过查询域名的信息，发现域名已经被设置clientHold，全球范围内停止解析了：</p><img src=\"https://cdn.renfei.net/upload/2022/15be61a80cc144308eeb7e6b9eff9b4c.webp\" alt=\"cnBeta.com域名被设置clientHold状态\" /><p style=\"text-indent: 2em;\">cnBeta的广告一直被人诟病，页面上大量的广告，有百度联盟广告、谷歌联盟广告、自营广告，页面上一半都是广告，还有很多网友在问，他们真的很缺钱吗？</p><img src=\"https://cdn.renfei.net/upload/2022/ff05e92ae50c4ac6893c06e400d8bbdd.webp\" alt=\"cnBeta.com广告需要那么多吗\" /><p style=\"text-indent: 2em;\">这次被关站，应该不是小错误，域名都被设置了 clientHold，应该是踩了红线了，根据网友们猜测，这次也是因为广告，把自己玩死了，据传言是因为一个广告位上出现了政治上的错误，是某些反 X 言论，导致的关站，我觉得非常有可能，一般的小错误不会造成把域名都 clientHold 了。</p><p><br/></p><p style=\"text-indent: 2em;\">有的朋友可能不太了解 clientHold 是啥意思，我解释下，这个是域名注册商设置的一种域名状态，一旦被设置 clientHold，那么全球范围这个域名都会停止解析，域名不能转出也不能修改，就等于被冻结了。</p><p style=\"text-indent: 2em;\">看域名的 whois 信息，注册商是 35互联，国内的注册局，只要接到有关部门的命令，是可以设置 clientHold 状态的。</p><p style=\"text-indent: 2em;\">估计站长想要回这个域名只有一条路，向全球管理局 ICANN 提交申诉，要求转出 35互联，不过意义不大，就算转出了，也是被墙的状态，国内的服务都用不了了。</p><p style=\"text-indent: 2em;\">各位站长一定要审查好广告内容，红线不能踩！</p>";
        String plainText = articleService.getPlainText(html);
        System.out.println(plainText);
        long wordCount = articleService.getWordCount(plainText);
        System.out.println(wordCount);
        String s = articleService.getReadTime(html);
        System.out.println(s);
    }
}
