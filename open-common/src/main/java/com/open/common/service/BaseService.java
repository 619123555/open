package com.open.common.service;

import com.github.pagehelper.PageInfo;
import java.util.List;
import tk.mybatis.mapper.entity.Example;

public interface BaseService<T> {

  /**
   * 新增记录(所有字段)
   *
   * @param obj obj
   * @return int
   */
  int insert(T obj);

  /**
   * 新增记录(非空字段)
   *
   * @param obj obj
   * @return int
   */
  int insertSelective(T obj);

  /**
   * 批量插入
   *
   * @param list
   * @return int
   */
  int batchInsertList(List<T> list);

  /**
   * 删除记录
   *
   * @param obj
   * @return int
   */
  int delete(T obj);

  /**
   * 根据主键删除记录
   *
   * @param obj Object
   * @return int
   */
  int deleteByPrimaryKey(Object obj);

  /**
   * 条件删除数据
   *
   * @param example exapmle
   * @return int
   */
  int deleteByExample(Object example);

  /**
   * 批量删除数据
   *
   * @param list list
   * @return int
   */
  int deleteBatchLists(List<T> list);

  /**
   * 删除主键集合的数据
   *
   * @param clazz class
   * @param ids ids
   * @return int
   */
  int deleteByIds(Class<T> clazz, List<Object> ids);

  /**
   * 根据主键修改记录
   *
   * @param obj T
   * @return int
   */
  int updateByPrimaryKey(T obj);

  /**
   * 根据主键修改非空字段的值
   *
   * @param obj T
   * @return int
   */
  int updateByPrimaryKeySelective(T obj);

  /**
   * 查询所有记录
   *
   * @return List
   */
  List<T> selectAll();

  /**
   * 条件查询list列表
   *
   * @param obj T
   * @return List<T>
   */
  List<T> select(T obj);

  /**
   * 条件查询
   *
   * @param example example
   * @return List
   */
  List<T> selectByExample(Example example);

  /**
   * 条件查询一条记录(理想中只存在一条记录)
   *
   * @param obj T
   * @return T
   */
  T selectOne(T obj);

  /**
   * 根据主键查询记录
   *
   * @param obj object
   * @return T
   */
  T selectByPrimaryKey(Object obj);

  /**
   * 查询记录条数
   *
   * @param obj T
   * @return PageInfo
   */
  int selectCount(T obj);

  /**
   * 分页
   *
   * @param obj T
   * @param pageNo 页码
   * @param pageSize
   * @return PageInfo
   */
  PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize);

  /**
   * 分页
   *
   * @param obj T
   * @param pageNo 页码
   * @param pageSize 每页条数
   * @param count count
   * @return PageInfo
   */
  PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize, boolean count);

  /**
   * 分页
   *
   * @param obj T
   * @param pageNo 页码
   * @param pageSize 每页条数
   * @param orderBy 排序条件 id DESC
   * @return PageInfo
   */
  PageInfo<T> selectEntityPage(T obj, int pageNo, int pageSize, String orderBy);

  /**
   * 分页
   *
   * @param obj T
   * @param pageNo 页码
   * @param pageSize 每页条数
   * @param count count
   * @param reasonable reasonable
   * @param pageSizeZero pageSizeZero
   * @return PageInfo
   */
  PageInfo<T> selectEntityPage(
      T obj, int pageNo, int pageSize, boolean count, Boolean reasonable, Boolean pageSizeZero);

  /**
   * 分页条件查询
   *
   * @param example example
   * @param pageNo pageNo
   * @param pageSize pageSize
   * @return PageInfo
   */
  PageInfo<T> selectEntityExamplePage(Example example, int pageNo, int pageSize);

  PageInfo<T> selectEntityExamplePage(Example example, int pageNo, int pageSize, String orderBy);
}
