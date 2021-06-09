package com.open.common.utils.signature;

import com.open.common.enums.OsEnum;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

@Slf4j
public class AesUtilsHelp {

    private static String CIPHER_WAY = "AES/CBC/PKCS5Padding";
    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    public static String createNonceStr() {
        String sl = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(sl.charAt(new Random().nextInt(sl.length())));
        }
        return sb.toString();
    }

    public static long createTimestamp() {
        return (System.currentTimeMillis() / 1000);
    }

    public static String sortMap(Map<String, String> param) {
        StringBuilder result = new StringBuilder();
        Collection<String> keySet = param.keySet();
        List<String> list = new ArrayList<>(keySet);
        Collections.sort(list);
        for (int i = 0; i < list.size(); ++i) {
            String key = list.get(i);
            if ("symmetricKey".equals(key)) {
                continue;
            }
            if (param.get(key) == null || "".equals(param.get(key).trim())) {
                continue;
            }
            result.append(key).append("=").append(param.get(key)).append("&");
        }
        return result.substring(0, result.length() - 1);
    }

    private static byte[] hexToBytes(String hex) {
        hex = hex.length() % 2 != 0 ? "0" + hex : hex;
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String encodeNew(String value, String key) {
        if (null == value || "".equals(value)) {
            return "";
        }
        byte[] valueByte = value.getBytes();
        byte[] sl = new byte[0];
        try {
            sl = encrypt3DES(valueByte, hexToBytes(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(sl);
    }

    /**
     * 3DES加密
     * @param input 待加密的字节
     * @param key 密钥
     * @return 加密后的字节
     */
    private static byte[] encrypt3DES(byte[] input, byte[] key) throws Exception {
        Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"));
        return c.doFinal(input);
    }

    public static String unipaySign(String value, String privateKey) {
        byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(value.getBytes("UTF-8"));
            byte[] signed = signature.sign();
            return Base64Utils.encodeToString(signed);
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return null;
    }

    public static String decode(String value, String key) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        byte[] valueByte = org.bouncycastle.util.encoders.Base64.decode(value);
        byte[] sl = new byte[0];
        try {
            sl = decrypt3dec(valueByte, hexToBytes(key));
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return new String(sl);
    }

    private static byte[] decrypt3dec(byte[] input, byte[] key) throws Exception {
        Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
        return c.doFinal(input);
    }

    public static String sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return bytesToHex(md.digest(data));
        } catch (Exception ex) {
            log.info("Never happen.", ex);
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        String hexArray = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int bi = b & 0xff;
            sb.append(hexArray.charAt(bi >> 4));
            sb.append(hexArray.charAt(bi & 0xf));
        }
        return sb.toString();
    }

    public static String getAgentOs(String agent) {
        if (agent.contains(OsEnum.Android.name()) || agent.contains(OsEnum.Linux.name())) {
            return OsEnum.Android.name();
        } else if (agent.contains(OsEnum.iPhone.name())) {
            return OsEnum.iPhone.name();
        } else if (agent.contains(OsEnum.Windows.name())) {
            return OsEnum.Windows.name();
        } else {
            return OsEnum.Other.name();
        }
    }
}
