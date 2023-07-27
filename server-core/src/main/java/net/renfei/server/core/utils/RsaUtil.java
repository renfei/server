package net.renfei.server.core.utils;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.SecretKey;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author renfei
 */
@Slf4j
public class RsaUtil {
    private final static String PRIVATE_HEADER = "-----BEGIN PRIVATE KEY-----";
    private final static String PRIVATE_FOOTER = "-----END PRIVATE KEY-----";
    private final static String PUBLIC_HEADER = "-----BEGIN PUBLIC KEY-----";
    private final static String PUBLIC_FOOTER = "-----END PUBLIC KEY-----";

    /**
     * 随机生成密钥对
     *
     * @param keySize 秘钥长度
     * @return
     */
    public static SecretKey genKeyPair(int keySize) {
        PrivateKey privateKey;
        PublicKey publicKey;
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keySize);
            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
            String publicKeyString = encoder.encodeToString(publicKey.getEncoded());
            String privateKeyString = encoder.encodeToString(privateKey.getEncoded());
            return SecretKey.builder()
                    .publicKey(PUBLIC_HEADER + "\n" + publicKeyString + "\n" + PUBLIC_FOOTER)
                    .privateKey(PRIVATE_HEADER + "\n" + privateKeyString + "\n" + PRIVATE_FOOTER)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥加密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptByPrivateKey(String str, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(privateKey));
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
        return new String(cipher.doFinal(Base64.getDecoder().decode(str)), StandardCharsets.UTF_8);
    }

    /**
     * RSA公钥解密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptByPublicKey(String str, String publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey(publicKey));
        return new String(cipher.doFinal(Base64.getDecoder().decode(str)), StandardCharsets.UTF_8);
    }

    public static PublicKey getPublicKey(String base64PublicKey) {
        base64PublicKey = base64PublicKey.replace("\n", "").replace(PUBLIC_HEADER, "").replace(PUBLIC_FOOTER, "");
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        base64PrivateKey = base64PrivateKey.replace("\n", "").replace(PRIVATE_HEADER, "").replace(PRIVATE_FOOTER, "");
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        try {
            assert keyFactory != null;
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        }
        return privateKey;
    }
}
