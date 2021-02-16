package com.trans.payment.boss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trans.payment.boss.service.BaseService;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

public class BaseServiceImpl<T> implements BaseService<T> {


  @Autowired
  protected Mapper<T> mapper;

  public int insert(T obj)
  {
    return this.mapper.insert(obj);
  }

  public int insertSelective(T obj)
  {
    return this.mapper.insertSelective(obj);
  }

  public int batchInsertList(List<T> list)
  {
    int result = 0;
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object record = localIterator.next();
      int count = this.mapper.insertSelective((T) record);
      result += count;
    }
    return result;
  }

  public int delete(T obj)
  {
    return this.mapper.delete(obj);
  }

  public int deleteByPrimaryKey(Object obj)
  {
    return this.mapper.deleteByPrimaryKey(obj);
  }

  public int deleteByExample(Object example)
  {
    return this.mapper.deleteByExample(example);
  }

  public int deleteByIds(Class<T> clazz, List<Object> ids)
  {
    Example example = new Example(clazz);
    example.createCriteria().andIn("id", ids);
    return this.mapper.deleteByExample(example);
  }

  public int deleteBatchLists(List<T> list)
  {
    int result = 0;
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object record = localIterator.next();
      int count = this.mapper.delete((T) record);
      if (count < 1) {
        throw new RuntimeException("delete data fail");
      }
      result += count;
    }
    return result;
  }

  public int updateByPrimaryKey(T obj)
  {
    return this.mapper.updateByPrimaryKey(obj);
  }

  public int updateByPrimaryKeySelective(T obj)
  {
    return this.mapper.updateByPrimaryKeySelective(obj);
  }

  public List<T> selectAll()
  {
    return this.mapper.selectAll();
  }

  public List<T> select(T obj)
  {
    return this.mapper.select(obj);
  }

  public List<T> selectByExample(Example example)
  {
    return this.mapper.selectByExample(example);
  }

  public T selectOneByExample(Object object)
  {
    return (T) this.mapper.selectOneByExample(object);
  }

  public T selectOne(T obj)
  {
    return (T) this.mapper.selectOne(obj);
  }

  public T selectByPrimaryKey(Object obj)
  {
    return (T) this.mapper.selectByPrimaryKey(obj);
  }

  public int selectCount(T obj)
  {
    return this.mapper.selectCount(obj);
  }

  public int selectCountByExample(Object example)
  {
    return this.mapper.selectCountByExample(example);
  }

  public PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize)
  {
    PageHelper.startPage(pageNo, pageSize);
    List list = this.mapper.select(obj);
    return new PageInfo(list);
  }

  public PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize, boolean count)
  {
    PageHelper.startPage(pageNo, pageSize, count);
    List list = this.mapper.select(obj);
    return new PageInfo(list);
  }

  public PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize, String orderBy)
  {
    PageHelper.startPage(pageNo, pageSize, orderBy);
    List list = this.mapper.select(obj);
    return new PageInfo(list);
  }

  public PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize, boolean count, Boolean reasonable, Boolean pageSizeZero)
  {
    PageHelper.startPage(pageNo, pageSize, count, reasonable, pageSizeZero);
    List list = this.mapper.select(obj);
    return new PageInfo(list);
  }

  public PageInfo<T> selectEntityExamplePage(Example example, int pageNo, int pageSize)
  {
    PageHelper.startPage(pageNo, pageSize);
    List list = this.mapper.selectByExample(example);
    return new PageInfo(list);
  }

  public PageInfo<T> selectEntityExamplePage(Example example, int pageNo, int pageSize, String orderBy)
  {
    PageHelper.startPage(pageNo, pageSize, orderBy);
    List list = this.mapper.selectByExample(example);
    return new PageInfo(list);
  }

}
