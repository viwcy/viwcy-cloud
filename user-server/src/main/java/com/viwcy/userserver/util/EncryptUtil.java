package com.viwcy.userserver.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO    RSA加密与解密
 * @Date 2020/9/1 16:35
 * @Author Fuqiang
 * <p>
 *
 * </p>
 */
@Component
public class EncryptUtil {

    private static Map<String, String> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        String encrypt = encrypts("123456", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIpZGb9L4ypKxN0DOOr19b+MDYjymOHe+IZMTnoPTQeo5SY79Mci0lH+/OQEhXR7RI76iFetRGxiyQgN0lK1DrstVNed5LmhgWn/u1us9IJj0wMGuQ+KEs5n3hBaxpIlktJcnf4U/flBc5SNpvaynaL0mbOmsZVl2+Un6vc1qlywIDAQAB");
        System.out.println(encrypt);
        String decrypt = decrypts(encrypt + "5fa53f135e1sa351f313es1g35e153s1f31e31f31SE31S1F1SD3G1H1FG313J2132FG1H321F321H", "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMilkZv0vjKkrE3QM46vX1v4wNiPKY4d74hkxOeg9NB6jlJjv0xyLSUf785ASFdHtEjvqIV61EbGLJCA3SUrUOuy1U153kuaGBaf+7W6z0gmPTAwa5D4oSzmfeEFrGkiWS0lyd/hT9+UFzlI2m9rKdovSZs6axlWXb5Sfq9zWqXLAgMBAAECgYBpoXCk0BYJh1QGqyNZbM4hFzY/TC8uWGgwQAzBfKUl1D909HpTfpBxyPxwcrsi5+brtu/bRazXjaEMeiryVHmGjJREF7g7l4MdicHlXY9yTPx598xi0MuN0NFwGlxbw3HDpugFf8ayS7ZpFMIYCKB+VK7XytbnzElB3rgzTW5RIQJBAPo0bGsB+BrycZpdE3IiQs05MTdh0wMD0nGeIevyUZ1Dfpm5rliAjxfMWsrQm4qLsPC58ZelGqrSXHcQehpNKIMCQQDNS0sh6ZVIdRqAZJKWZnlRIUSGR6YWoVsEhy0b3EROvsHocWQBfCbuZ5fJiL7ytBoZYQSTdCJZzKk4tmN8vrsZAkBnA8SLvlDPj3iUjLPf7xk/88c+vSkq+ZzgqLHgunE95bcvRdLxn2TK8JmFHaScw20DgBtnS1QSdvAZGZCcWZ5PAkBRDAWYWNAPr/OLdqkMaV5BskGMRaMDUiZNrlQXqKjq8ZI6AvH5SBtSiWE6Nhs0U/ftxwh4ciPRkePIuPCj4eSZAkAznQcefB3VfelXd3MR7zsUpCjT9nCtQE40Q/MnsZ5ocWVgtiKhTGraTUjYsoDckUM1BfIkYhG8yzJ38MJ3hYj6");
        System.out.println(decrypt);
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public Map<String,String> genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        map.put("publicKey", publicKeyString);
        map.put("privateKey", privateKeyString);
        return map;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static String encrypts(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypts(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * base64 编码
     *
     * @param content 原字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public String encode(String content) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(content.getBytes("UTF-8")));
    }

    /**
     * base64 解码
     *
     * @param content 加密之后的字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public String decode(String content) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(content.getBytes("UTF-8")));
    }
}
