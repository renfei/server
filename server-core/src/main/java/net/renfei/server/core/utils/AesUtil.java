package net.renfei.server.core.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author renfei
 */
public class AesUtil {
    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    /**
     * 生成一个 Key
     *
     * @return
     */
    public static String generateKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(AES_KEY_SIZE);
        SecretKey key = keyGenerator.generateKey();
        return base64Encode(key.getEncoded());
    }

    /**
     * 生成一个IV
     *
     * @return
     */
    public static String generateIv() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return base64Encode(iv);
    }


    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @param iv         初始向量
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] decodedKey = base64Decode(encryptKey);
        SecretKeySpec keySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        byte[] ivArr = base64Decode(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, ivArr);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥，必须16位
     * @param iv         初始向量
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey, String iv) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey, iv));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @param iv           初始向量
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] decodedKey = base64Decode(decryptKey);
        SecretKeySpec keySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        byte[] ivArr = base64Decode(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, ivArr);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        return new String(cipher.doFinal(encryptBytes));
    }


    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥，必须16位
     * @param iv         初始向量
     * @return 解密后的string
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey, String iv) throws Exception {
        return encryptStr == null ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey, iv);
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return base64Code == null ? null : Base64.decodeBase64(base64Code);
    }
}
