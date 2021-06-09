package com.open.common.utils.signature;

import com.open.common.utils.signature.key.KeyReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/** @author admin */
public class RsaSignWithFile {

  /**
   * 签名
   *
   * @param data 原始数据
   * @param privateKey 密钥
   * @return 签名后的数据
   * @throws Exception
   */
  public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
    Signature signature = Signature.getInstance("SHA1withRSA");
    signature.initSign(privateKey);
    signature.update(data);
    return signature.sign();
  }

  /**
   * 验签
   *
   * @param data 原始数据
   * @param signedData 签名数据
   * @param publicKey 公钥
   * @return 验签结果：true 通过，false 不通过
   * @throws Exception
   */
  public static boolean validateSign(byte[] data, byte[] signedData, PublicKey publicKey)
      throws Exception {
    Signature signature = Signature.getInstance("SHA1withRSA");
    signature.initVerify(publicKey);
    signature.update(data);
    return signature.verify(signedData);
  }

  /**
   * 十进制字节数组转换为十六进制字符串
   *
   * @param arr
   * @return
   */
  public static String byteToHex(byte[] arr) {
    int i;
    String byteStr;
    StringBuffer strBuf = new StringBuffer();
    for (i = 0; i < arr.length; i++) {
      byteStr = Integer.toHexString(arr[i] & 0x00ff);
      if (byteStr.length() != 2) {
        strBuf.append('0').append(byteStr);
      } else {
        strBuf.append(byteStr);
      }
    }
    return new String(strBuf);
  }

  /**
   * 十六进制字符串转换为十进制字节数组
   *
   * @param str
   * @return
   */
  public static byte[] hexToByte(String str) {
    int i;
    int len = str.length() / 2;
    byte[] outbuf = new byte[len];
    for (i = 0; i < len; i++) {
      String tmpbuf = str.substring(i * 2, i * 2 + 2);
      outbuf[i] = (byte) Integer.parseInt(tmpbuf, 16);
    }
    return outbuf;
  }

  /**
   * 获取密钥
   *
   * @param path 路径
   * @param password 密钥密码
   * @return 密钥
   * @throws Throwable
   */
  public static PrivateKey getPrivateKey(String path, String password) throws Exception {
    return KeyReader.readPrivateKeyfromPKCS12StoredFile(path, password);
  }

  /**
   * 读取公钥
   *
   * @param path 路径
   * @return
   * @throws Throwable
   */
  public static PublicKey getPublicKey(String path) throws Exception {
    return (PublicKey) KeyReader.fromCerStoredFile(path);
  }
}
