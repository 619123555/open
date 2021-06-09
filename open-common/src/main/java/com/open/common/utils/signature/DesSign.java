package com.open.common.utils.signature;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/** @author admin @创建人：YYX @创建时间：2016/3/18 14:46 */
public class DesSign {

  /**
   * DES 算法 <br>
   * 可替换为以下任意一种算法，同时key值的size相应改变。
   *
   * <p>
   *
   * <pre>
   * DES                  key size must be equal to 56
   * DESede(TripleDES)    key size must be equal to 112 or 168
   * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
   * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
   * RC2                  key size must be between 40 and 1024 bits
   * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
   * </pre>
   */
  public String algorithm = "DES";

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public DesSign() {}

  public DesSign(String algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * DES 算法转换密钥<br>
   *
   * @param key
   * @return
   * @throws Exception
   */
  private Key toKey(byte[] key) throws Exception {
    SecretKey secretKey = null;
    if ("DES".equals(algorithm) || "DESede".equals(algorithm)) {
      DESKeySpec dks = new DESKeySpec(key);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
      secretKey = keyFactory.generateSecret(dks);
    } else {
      // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
      secretKey = new SecretKeySpec(key, algorithm);
    }
    return secretKey;
  }

  /**
   * DES 算法解密
   *
   * @param cryptData
   * @param key
   * @return
   * @throws Exception
   */
  public String decrypt(String cryptData, String key) throws Exception {
    Key k = toKey(Base64Util.decryptBASE64(key));
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, k);
    return new String(cipher.doFinal(Base64Util.decryptBASE64(cryptData)));
  }

  /**
   * DES 算法加密
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public String encrypt(byte[] data, String key) throws Exception {
    Key k = toKey(Base64Util.decryptBASE64(key));
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, k);
    return Base64Util.encryptBASE64(cipher.doFinal(data));
  }

  /** DES算法密钥 */
  private static final byte[] DES_KEY = {21, 1, -110, 82, -32, -85, -128, -65};

  /**
   * 数据加密，算法（DES）
   *
   * @param data 要进行加密的数据
   * @return 加密后的数据
   */
  public static String encryptBasedDes(String data) {
    String encryptedData = null;
    try {
      // DES算法要求有一个可信任的随机数源
      SecureRandom sr = new SecureRandom();
      DESKeySpec deskey = new DESKeySpec(DES_KEY);
      // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      SecretKey key = keyFactory.generateSecret(deskey);
      // 加密对象
      Cipher cipher = Cipher.getInstance("DES");
      cipher.init(Cipher.ENCRYPT_MODE, key, sr);
      // 加密，并把字节数组编码成字符串
      encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
    } catch (Exception e) {
      throw new RuntimeException("加密错误，错误信息：", e);
    }
    return encryptedData;
  }

  /**
   * 数据解密，算法（DES）
   *
   * @param cryptData 加密数据
   * @return 解密后的数据
   */
  public static String decryptBasedDes(String cryptData) {
    String decryptedData = null;
    try {
      // DES算法要求有一个可信任的随机数源
      SecureRandom sr = new SecureRandom();
      DESKeySpec deskey = new DESKeySpec(DES_KEY);
      // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      SecretKey key = keyFactory.generateSecret(deskey);
      // 解密对象
      Cipher cipher = Cipher.getInstance("DES");
      cipher.init(Cipher.DECRYPT_MODE, key, sr);
      // 把字符串解码为字节数组，并解密
      decryptedData =
          new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
    } catch (Exception e) {
      throw new RuntimeException("解密错误，错误信息：", e);
    }
    return decryptedData;
  }

  /**
   * DES 算法生成密钥
   *
   * @return
   * @throws Exception
   */
  public String initKey() throws Exception {
    return initKey(null);
  }

  /**
   * DES 算法生成密钥
   *
   * @param seed
   * @return
   * @throws Exception
   */
  public String initKey(String seed) throws Exception {
    SecureRandom secureRandom = null;
    if (seed != null) {
      secureRandom = new SecureRandom(Base64Util.decryptBASE64(seed));
    } else {
      secureRandom = new SecureRandom();
    }
    KeyGenerator kg = KeyGenerator.getInstance(algorithm);
    kg.init(secureRandom);
    SecretKey secretKey = kg.generateKey();
    return Base64Util.encryptBASE64(secretKey.getEncoded());
  }

  public static void main(String[] args) {
    String aaa = "!@#";
    for (int i = 0; i < 100; i++) {
      System.out.println(encryptBasedDes(aaa + i));
    }
  }
}
