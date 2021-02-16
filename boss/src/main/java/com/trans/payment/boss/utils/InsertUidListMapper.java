package com.trans.payment.boss.utils;

import java.util.List;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.additional.insert.InsertListProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface InsertUidListMapper<T>
{
  @InsertProvider(type= InsertListProvider.class, method="dynamicSQL")
  public int insertList(List<? extends T> paramList);
}