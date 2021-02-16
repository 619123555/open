package com.trans.payment.common.utils.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/** @author admin */
public class RsaSign {

  public String signType = "SHA1WithRSA";

  String inputCharset = "UTF-8";

  public String getSignType() {
    return signType;
  }

  public void setSignType(String signType) {
    this.signType = signType;
  }

  public RsaSign() {}

  public RsaSign(String signType) {
    this.signType = signType;
  }

  /**
   * RSA签名
   *
   * @param content 待签名数据
   * @param privateKey 私钥
   * @return 签名值
   */
  public String sign(String content, String privateKey) {
    try {
      PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.decryptBASE64(privateKey));
      KeyFactory keyf = KeyFactory.getInstance("RSA");
      PrivateKey priKey = keyf.generatePrivate(priPKCS8);

      java.security.Signature signature = java.security.Signature.getInstance(signType);

      signature.initSign(priKey);
      signature.update(content.getBytes(inputCharset));

      byte[] signed = signature.sign();

      return Base64Util.encryptBASE64(signed);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * RSA验签名检查
   *
   * @param content 待签名数据
   * @param sign 签名值
   * @param publicKey 公钥
   * @return 布尔值
   */
  public boolean verify(String content, String sign, String publicKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      byte[] encodedKey = Base64Util.decryptBASE64(publicKey);
      PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

      java.security.Signature signature = java.security.Signature.getInstance(signType);

      signature.initVerify(pubKey);
      signature.update(content.getBytes(inputCharset));

      boolean bverify = signature.verify(Base64Util.decryptBASE64(sign));
      return bverify;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * 公钥加密
   *
   * @param content 明文
   * @param publicKey 公钥
   * @return 解密后的字符串
   */
  public String encrypt(String content, String publicKey) throws Exception {
    PublicKey pubKey = getPublicKey(publicKey);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] enBytes = cipher.doFinal(content.getBytes(inputCharset));
    return Base64Util.encryptBASE64(enBytes);
  }

  /**
   * 解密
   *
   * @param content 密文
   * @param privateKey 私钥
   * @param inputCharset 编码格式
   * @return 解密后的字符串
   */
  public String decrypt(String content, String privateKey, String inputCharset) throws Exception {
    PrivateKey prikey = getPrivateKey(privateKey);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, prikey);

    InputStream ins = new ByteArrayInputStream(Base64Util.decryptBASE64(content));
    ByteArrayOutputStream writer = new ByteArrayOutputStream();
    // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
    byte[] buf = new byte[128];
    int bufl;

    while ((bufl = ins.read(buf)) != -1) {
      byte[] block;

      if (buf.length == bufl) {
        block = buf;
      } else {
        block = new byte[bufl];
        for (int i = 0; i < bufl; i++) {
          block[i] = buf[i];
        }
      }

      writer.write(cipher.doFinal(block));
    }

    return new String(writer.toByteArray(), inputCharset);
  }

  /**
   * 得到私钥
   *
   * @param key 密钥字符串（经过base64编码）
   * @throws Exception
   */
  public static PrivateKey getPrivateKey(String key) throws Exception {

    byte[] keyBytes;

    keyBytes = Base64Util.decryptBASE64(key);

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

    return privateKey;
  }

  /**
   * 得到公钥
   *
   * @param key 密钥字符串（经过base64编码）
   * @throws Exception
   */
  public static PublicKey getPublicKey(String key) throws Exception {
    byte[] keyBytes;
    keyBytes = Base64Util.decryptBASE64(key);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    PublicKey pubKey = keyFactory.generatePublic(keySpec);
    return pubKey;
  }

  /*
  public static void main(String[] args) throws Exception {

      RsaSign rsaSign = new RsaSign();

      // demo
      final String pck8 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJZjwzwIqMuU5lrWj18NBpzg3CM3dvRlDVtCkU/UrQKvZwgkNxIV+8f050tlrwGq2XL2aFtkd3FqvEbpPHARCaPd1pjX1+6Kd8TUTi7B5dicLecMhQ0KhIIBXCNnSc93YGhe5gJLn1gg2hHZvsrk0CPxpw0aopiGYkzDuu3QS/ZVAgMBAAECgYAtw4f6scvjv5jllZslWopjnM2AbfVl44ZP+g7MEzTet0+MG7EicMwgifcEa5n40OvA3xjcjtMGEWvfzEPW+mhLHN7r7r18yfkkMhKvJ8DL/Or53fDmM/miDsDmJoufHsouPXRj650w7TPN2qFXlV1x7xwvQ9r/inj4KZGVFSrk9QJBAMZKN9Uu0dC4/6pVYB6GPiVm4S/Oey1ZSFoVY2sb11VgvZqsTG3xHVuSFRp+k16MofzKnzZjARPexxx8ZGn0s48CQQDCKLFLc5OtILLEMTIDbiNBGGWSouBpoPzUJ1PEV+XZJbBKeaUGN5L3O1ewTnnAjeRNoykS+6fBruoXuMIgunXbAkEAryKGRchgkNWVGsDlcJBo+2fupObebKNbABW8BbJFcqdLT/22U7SOlX5lHU2GpA/aUwgzrzmqjf5KJqLHnb3/iQJAR5pMMCAygPGDG9lssVknxTTjGJ3alvnY48CRIuKrkFZW2lBcLJvIJkcLQqjeO8XYQU1/s1F9f28rQ45OgVViawJAeidc0gHxv0Oaww2cKTvQP3Q411QG8egFIvluNwToCuWOeLISOROdk3Xf2vRgZdZVeHsCuxVvLWd8FJSlGW9rXw==";
      final String pubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWY8M8CKjLlOZa1o9fDQac4NwjN3b0ZQ1bQpFP1K0Cr2cIJDcSFfvH9OdLZa8Bqtly9mhbZHdxarxG6TxwEQmj3daY19fuinfE1E4uweXYnC3nDIUNCoSCAVwjZ0nPd2BoXuYCS59YINoR2b7K5NAj8acNGqKYhmJMw7rt0Ev2VQIDAQAB";

      // sign and validate
      // 使用私钥签名
      String sign = rsaSign.sign("hello, test test", pck8);
      // 使用公钥验证
      boolean verifyRes = rsaSign.verify("hello, test test", sign, pubkey);
      System.out.println("Verify Result " + verifyRes);

      // encrypt and decrypt
      // 使用公钥加密
      String secuirty = rsaSign.encrypt("hello, world 中国", pubkey);
      System.out.println("security is: " + secuirty);
      // 使用私钥解密
      String result  = rsaSign.decrypt(secuirty, pck8, "utf-8");
      System.out.println(result);
  }
  */
}
