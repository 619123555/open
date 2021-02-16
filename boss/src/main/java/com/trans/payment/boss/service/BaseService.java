package com.trans.payment.boss.service;

import com.github.pagehelper.PageInfo;
import java.util.List;
import org.apache.poi.ss.formula.functions.T;
import tk.mybatis.mapper.entity.Example;

public interface BaseService<T> {
   int insert(T paramT);

   int insertSelective(T paramT);

   int batchInsertList(List<T> paramList);

   int delete(T paramT);

   int deleteByPrimaryKey(Object paramObject);

   int deleteByExample(Object paramObject);

   int deleteBatchLists(List<T> paramList);

   int deleteByIds(Class<T> paramClass, List<Object> paramList);

   int updateByPrimaryKey(T paramT);

   int updateByPrimaryKeySelective(T paramT);

   List<T> selectAll();

   List<T> select(T paramT);

   List<T> selectByExample(Example paramExample);

   T selectOneByExample(Object paramObject);

   T selectOne(T paramT);

   T selectByPrimaryKey(Object paramObject);

   int selectCount(T paramT);

   int selectCountByExample(Object paramObject);

   PageInfo<T> selectEntityPage(T paramT, int paramInt1, int paramInt2);

   PageInfo<T> selectEntityPage(T paramT, int paramInt1, int paramInt2, boolean paramBoolean);

   PageInfo<T> selectEntityPage(T paramT, int paramInt1, int paramInt2, String paramString);

   PageInfo<T> selectEntityPage(T paramT, int paramInt1, int paramInt2, boolean paramBoolean, Boolean paramBoolean1, Boolean paramBoolean2);

   PageInfo<T> selectEntityExamplePage(Example paramExample, int paramInt1, int paramInt2);

   PageInfo<T> selectEntityExamplePage(Example paramExample, int paramInt1, int paramInt2, String paramString);
}
