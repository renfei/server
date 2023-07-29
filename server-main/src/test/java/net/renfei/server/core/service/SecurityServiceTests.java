package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.SecretKey;
import net.renfei.server.core.utils.AesUtil;
import net.renfei.server.core.utils.RsaUtil;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 安全服务测试
 *
 * @author renfei
 */
@Slf4j
public class SecurityServiceTests extends ApplicationTest {
    @Autowired
    private SecurityService securityService;

    /**
     * 弱密码识别测试
     */
    @Test
    public void weakPasswordCheckTest() {
        String info = "WeakPasswordCheck [{}] is weak password: {}.";
        log.info("-----< WeakPassword Test >-----");
        String txt = "password";
        boolean checked = securityService.weakPasswordCheck(txt);
        log.info(info, txt, checked);
        assert checked;
        txt = "123456789";
        checked = securityService.weakPasswordCheck(txt);
        log.info(info, txt, checked);
        assert checked;
        txt = "qazwsx";
        checked = securityService.weakPasswordCheck(txt);
        log.info(info, txt, checked);
        assert checked;
        txt = "passw0rd";
        checked = securityService.weakPasswordCheck(txt);
        log.info(info, txt, checked);
        assert checked;
        txt = "gkduWen!%#$dsgd7553";
        checked = securityService.weakPasswordCheck(txt);
        log.info(info, txt, checked);
        assert !checked;
    }

    /**
     * 客户端与服务器交换秘钥单元测试
     *
     * @throws Exception
     */
    @Test
    public void clientServerSecretKeyTest() throws Exception {
        log.info("-----< Secret Key Test >-----");
        log.info("请求服务器端公钥：");
        SecretKey serverSecretKey = securityService.requestServerSecretKey();
        assert serverSecretKey.getPrivateKey() == null;
        log.info("服务器端公钥：\nUUID: {}\n公钥：{}", serverSecretKey.getUuid(), serverSecretKey.getPublicKey());
        // 生成客户端秘钥对
        log.info("生成客户端秘钥对：");
        SecretKey clientSecretKey = RsaUtil.genKeyPair(2048);
        assert clientSecretKey != null;
        log.info("客户端秘钥对：\n私钥: {}\n公钥：{}", clientSecretKey.getPrivateKey(), clientSecretKey.getPublicKey());
        String clientSecretKeyEncrypt = RsaUtil.encrypt(clientSecretKey.getPublicKey(), serverSecretKey.getPublicKey());
        log.info("使用服务器公钥加密客户端公钥：{}", clientSecretKeyEncrypt);
        SecretKey aesSecretKey = securityService.settingClientSecretKey(SecretKey.builder()
                .uuid(serverSecretKey.getUuid())
                .publicKey(clientSecretKeyEncrypt)
                .build());
        log.info("上报客户端公钥，获得AES Key 密文：{}", aesSecretKey.getPublicKey());
        log.info("上报客户端公钥，获得AES IV 密文：{}", aesSecretKey.getPrivateKey());
        String aesKey = RsaUtil.decrypt(aesSecretKey.getPublicKey(), clientSecretKey.getPrivateKey());
        String aesIv = RsaUtil.decrypt(aesSecretKey.getPrivateKey(), clientSecretKey.getPrivateKey());
        log.info("使用客户端私钥解密AES秘钥Key：{}", aesKey);
        log.info("使用客户端私钥解密AES秘钥IV：{}", aesIv);
        String password = "password";
        String aesEncrypt = AesUtil.encrypt(password, aesKey, aesIv);
        log.info("使用AES加密：{}，密文：{}", password, aesEncrypt);
        String decrypt = securityService.decryptAesByKeyId(aesEncrypt, aesSecretKey.getUuid());
        log.info("将加密结果上报给服务器，解密得到：{}", decrypt);
        assert password.equals(decrypt);
    }
}
