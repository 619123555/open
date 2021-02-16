package com.trans.payment.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/** @author admin */
public class SocketUtils {

  public static final int MAX_SIZE = 4096;

  public static byte[] readNIOStream(InputStream in) throws IOException {
    byte[] byRet = new byte[0];
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    ReadableByteChannel readChannel = Channels.newChannel(in);
    ByteArrayOutputStream out = new ByteArrayOutputStream(MAX_SIZE);
    WritableByteChannel writeChannel = Channels.newChannel(out);
    try {
      while ((readChannel.read(buffer)) > 0 || buffer.position() != 0) {
        buffer.flip();
        writeChannel.write(buffer);
        buffer.compact();
      }
      byRet = out.toByteArray();
    } catch (EOFException e) {
      e.printStackTrace();
    } catch (IOException e) {
      throw e;
    }
    if (byRet.length == 0) {
      throw new IOException("读取数据失败，输入流已经到达结尾");
    }
    return byRet;
  }

  public static byte[] readStream(InputStream stream) throws IOException {
    // 返回字节流
    byte[] byRet = new byte[0];
    // 套接字输入流
    InputStream in;
    if (stream instanceof BufferedInputStream) {
      in = (BufferedInputStream) stream;
    } else {
      in = new BufferedInputStream(stream);
    }
    // in = stream;
    ByteArrayOutputStream out = new ByteArrayOutputStream(4096);

    byte[] byRead = new byte[4096];
    try {
      int len;
      while ((len = in.read(byRead)) > 0) {
        out.write(byRead, 0, len);
        if (len < 4096 && in.available() == 0) {
          break;
        }
      }
      out.close();
      byRet = out.toByteArray();
    } catch (EOFException e) {
      e.printStackTrace();
    } catch (IOException e) {
      throw e;
    }
    if (byRet.length == 0) {
      throw new IOException("读取数据失败，输入流已经到达结尾");
    }
    return (byRet);
  }

  public static byte[] read(InputStream in, int len) throws IOException {
    byte[] byRead = new byte[len];
    int i = 0;
    int ch;
    while ((ch = in.read()) > 0) {
      byRead[i++] = (byte) ch;
    }
    if (i < len) {
      byte[] ret = new byte[i];
      System.arraycopy(byRead, 0, ret, 0, i);
      return ret;
    }
    return byRead;
  }

  public static byte[] readFully(InputStream in, int len) throws IOException {
    byte[] byRead = new byte[len];
    DataInputStream dis = new DataInputStream(in);
    dis.readFully(byRead);
    return byRead;
  }

  public static byte[] addBytes(byte[] bySrc, byte[] byAdd) {
    int nSrc = bySrc.length;
    byte[] byRet = new byte[bySrc.length + byAdd.length];
    System.arraycopy(bySrc, 0, byRet, 0, bySrc.length);
    System.arraycopy(byAdd, 0, byRet, nSrc, byAdd.length);
    return byRet;
  }

  public static byte[] exchange(String sIp, int nPort, byte[] byData, int nTimeOut)
      throws IOException {
    Socket sock = new Socket(sIp, nPort);
    byte[] byRecv;
    try {
      sock.setSoTimeout(nTimeOut);
      BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

      out.write(byData, 0, byData.length);
      out.flush();

      byRecv = readStream(in);
      in.close();
      out.close();
    } finally {
      sock.close();
    }

    return byRecv;
  }

  public static byte[] exchangeFix(String sIp, int nPort, byte[] byData, int nTimeOut)
      throws IOException {
    Socket sock = new Socket(sIp, nPort);
    byte[] byRecv;
    try {
      sock.setSoTimeout(nTimeOut);
      DataInputStream in = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

      out.writeInt(byData.length);
      out.write(byData, 0, byData.length);
      out.flush();

      int nRecv = in.readInt();
      byRecv = new byte[nRecv];
      in.readFully(byRecv, 0, nRecv);
      in.close();
      out.close();
    } finally {
      sock.close();
    }
    return byRecv;
  }

  public static byte[] exchangeFix(Socket sock, byte[] byData, int nTimeOut) throws IOException {
    sock.setSoTimeout(nTimeOut);
    DataInputStream in = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

    out.writeInt(byData.length);
    out.write(byData, 0, byData.length);
    out.flush();

    int nRecv = in.readInt();
    byte[] byRecv = new byte[nRecv];
    in.readFully(byRecv, 0, nRecv);

    return byRecv;
  }

  public static byte[] exchange(Socket sock, byte[] byData, int nTimeOut) throws IOException {
    sock.setSoTimeout(nTimeOut);
    BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

    out.write(byData, 0, byData.length);
    out.flush();
    byte[] byRecv = readStream(in);
    return byRecv;
  }

  public static byte[] readStream(InputStream in, byte[] endTags) throws IOException {
    if (endTags == null || endTags.length == 0) {
      return readStream(in);
    }
    boolean end = false;
    byte[] out = new byte[0];
    int n = endTags.length;
    do {
      byte[] ret = readStream(in);
      int total = ret.length;
      if (total < n) {
        end = false;
        out = addBytes(out, ret);
        continue;
      }
      for (int i = n; i > 0; i--) {
        if (endTags[n - i] != ret[total - i]) {
          end = false;
          break;
        }
        end = true;
      }
      if (end) {
        byte[] bNew = new byte[total - n];
        for (int i = 0; i < bNew.length; i++) {
          bNew[i] = ret[i];
        }
        out = addBytes(out, bNew);
      } else {
        out = addBytes(out, ret);
      }

    } while (!end);
    return out;
  }
}
