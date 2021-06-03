package com.trans.payment.common.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdWorker {

  private static final Logger logger = LoggerFactory.getLogger(IdWorker.class);

  /** 时间起始标记点，作为基准 */
  private static final long twepoch = 1514338236397L;
  /** 机器标识位数 */
  private static final long workerIdBits = 5L;
  /** 数据中心标识位数 */
  private static final long datacenterIdBits = 5L;
  /** 机器ID最大值 */
  private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
  /** 数据中心ID最大值 */
  private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
  /** 毫秒内自增位 */
  private static final long sequenceBits = 12L;
  /** 机器ID偏左移12位 */
  private static final long workerIdShift = sequenceBits;
  /** 数据中心ID左移17位 */
  private static final long datacenterIdShift = sequenceBits + workerIdBits;
  /** 时间毫秒左移22位 */
  private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

  private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

  private static long lastTimestamp = -1L;
  /** 0，并发控制 */
  private long sequence = 0L;

  private final long workerId;
  /** 数据标识id部分 */
  private final long datacenterId;

  public IdWorker() {
    this.datacenterId = getDatacenterId(maxDatacenterId);
    this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
  }
  /**
   * @param workerId 工作机器ID
   * @param datacenterId 序列号
   */
  public IdWorker(long workerId, long datacenterId) {
    if (workerId > maxWorkerId || workerId < 0) {
      throw new IllegalArgumentException(
          String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
    }
    if (datacenterId > maxDatacenterId || datacenterId < 0) {
      throw new IllegalArgumentException(
          String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
    }
    this.workerId = workerId;
    this.datacenterId = datacenterId;
  }

  public synchronized Long nextId() {
    long timestamp = timeGen();
    if (timestamp < lastTimestamp) {
      throw new RuntimeException(
          String.format(
              "Clock moved backwards.  Refusing to generate id for %d milliseconds",
              lastTimestamp - timestamp));
    }
    if (lastTimestamp == timestamp) {
      sequence = (sequence + 1) & sequenceMask;
      if (sequence == 0) {
        timestamp = tilNextMillis(lastTimestamp);
      }
    } else {
      sequence = 0L;
    }
    lastTimestamp = timestamp;
    return ((timestamp - twepoch) << timestampLeftShift)
        | (datacenterId << datacenterIdShift)
        | (workerId << workerIdShift)
        | sequence;
  }

  private Long tilNextMillis(final long lastTimestamp) {
    long timestamp = this.timeGen();
    while (timestamp <= lastTimestamp) {
      timestamp = this.timeGen();
    }
    return timestamp;
  }

  private long timeGen() {
    return System.currentTimeMillis();
  }

  private static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
    StringBuilder mpid = new StringBuilder();
    mpid.append(datacenterId);
    String name = ManagementFactory.getRuntimeMXBean().getName();
    if (!name.isEmpty()) {
      // jvmPid
      mpid.append(name.split("@")[0]);
    }
    return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
  }

  private static long getDatacenterId(long maxDatacenterId) {
    long id = 0L;
    try {
      InetAddress ip = InetAddress.getLocalHost();
      NetworkInterface network = NetworkInterface.getByInetAddress(ip);
      if (network == null) {
        id = 1L;
      } else {
        byte[] mac = network.getHardwareAddress();
        id =
            ((0x000000FF & (long) mac[mac.length - 1])
                    | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8)))
                >> 6;
        id = id % (maxDatacenterId + 1);
      }
    } catch (Exception e) {
      logger.error("getDatacenterId", e);
    }
    return id;
  }

  public String systemTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    return sdf.format(new Date());
  }
}
