package com.open.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.common.service.BaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

public class BaseServiceImpl<T> implements BaseService<T> {

  @Autowired
  protected Mapper<T> mapper;

  @Override
  public int insert(T obj) {
    return this.mapper.insert(obj);
  }

  @Override
  public int insertSelective(T obj) {
    return this.mapper.insertSelective(obj);
  }

  @Override
  public int batchInsertList(List<T> list) {
    int result = 0;
    for (T record : list) {
      int count = this.mapper.insertSelective(record);
      result += count;
    }
    return result;
  }

  @Override
  public int delete(T obj) {
    return this.mapper.delete(obj);
  }

  @Override
  public int deleteByPrimaryKey(Object obj) {
    return this.mapper.deleteByPrimaryKey(obj);
  }

  @Override
  public int deleteByExample(Object example) {
    return this.mapper.deleteByExample(example);
  }

  @Override
  public int deleteByIds(Class<T> clazz, List<Object> ids) {
    // 等效于where id in (#{ids})
    Example example = new Example(clazz);
    example.createCriteria().andIn("id", ids);
    return this.mapper.deleteByExample(example);
  }

  @Override
  public int deleteBatchLists(List<T> list) {
    int result = 0;
    for (T record : list) {
      int count = mapper.delete(record);
      if (count < 1) {
        throw new RuntimeException("delete data fail");
      }
      result += count;
    }
    return result;
  }

  @Override
  public int updateByPrimaryKey(T obj) {
    return this.mapper.updateByPrimaryKey(obj);
  }

  @Override
  public int updateByPrimaryKeySelective(T obj) {
    return this.mapper.updateByPrimaryKeySelective(obj);
  }

  @Override
  public List<T> selectAll() {
    return this.mapper.selectAll();
  }

  @Override
  public List<T> select(T obj) {
    return this.mapper.select(obj);
  }

  @Override
  public List<T> selectByExample(Example example) {
    return this.mapper.selectByExample(example);
  }

  @Override
  public T selectOne(T obj) {
    return this.mapper.selectOne(obj);
  }

  @Override
  public T selectByPrimaryKey(Object obj) {
    return this.mapper.selectByPrimaryKey(obj);
  }

  @Override
  public int selectCount(T obj) {
    return this.mapper.selectCount(obj);
  }

  @Override
  public PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize) {
    PageHelper.startPage(pageNo, pageSize);
    List<T> list = this.mapper.select(obj);
    return new PageInfo<T>(list);
  }

  @Override
  public PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize, boolean count) {
    PageHelper.startPage(pageNo, pageSize, count);
    List<T> list = this.mapper.select(obj);
    return new PageInfo<T>(list);
  }

  @Override
  public PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize, String orderBy) {
    PageHelper.startPage(pageNo, pageSize, orderBy);
    List<T> list = this.mapper.select(obj);
    return new PageInfo<T>(list);
  }

  @Override
  public PageInfo<T> selectEntityPage(
      T obj, int pageNo, int pageSize, boolean count, Boolean reasonable, Boolean pageSizeZero) {
    PageHelper.startPage(pageNo, pageSize, count, reasonable, pageSizeZero);
    List<T> list = this.mapper.select(obj);
    return new PageInfo<T>(list);
  }

  @Override
  public PageInfo<T> selectEntityExamplePage(Example example, int pageNo, int pageSize) {
    PageHelper.startPage(pageNo, pageSize);
    List<T> list = this.mapper.selectByExample(example);
    return new PageInfo<T>(list);
  }

  @Override
  public PageInfo<T> selectEntityExamplePage(
      Example example, int pageNo, int pageSize, String orderBy) {
    PageHelper.startPage(pageNo, pageSize, orderBy);
    List<T> list = this.mapper.selectByExample(example);
    return new PageInfo<T>(list);
  }
}
