package com.open.boss.mapper;

import com.open.boss.entity.Conf;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ConfMapper extends Mapper<Conf> {
  int deleteConf(Conf con);

  /**
   * 根据条件获取Conf总数
   *
   * @param conf
   * @return
   */
  int getCount(Conf conf);

  /**
   * 获取字典类型scope列表
   *
   * @return
   */
  List<String> getScopes();

  /**
   * 修改配置状态
   *
   * @param conf
   * @return
   */
  int enable(Conf conf);

  /**
   * 根据key获取配置对象
   *
   * @param confKey
   * @return
   */
  Conf findConfigByKey(@Param("confKey") String confKey);

  /**
   * 获取当前日期及后六个月年月 格式 yyyy-MM
   *
   * @return
   */
  List<String> getYearMonthFromNow();
}
