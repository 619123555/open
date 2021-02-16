package com.trans.payment.boss.mapper;

import com.trans.payment.boss.entity.Dict;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tk.mybatis.mapper.common.Mapper;

public interface DictMapper extends Mapper<Dict> {

    List<Dict> findList(Dict dict);

    int enable(Dict dict);

    /**
     * 根据机构过滤字典信息
     * @param dict
     * @return
     */
    List<Dict> selectDictByOrgId(Dict dict);

    /**
     * 查询字典存不存在
     * @param dict
     * @return
     */
    Long findDictCount(Dict dict);

    /**
     * 根据渠道号查询渠道名称
     * @param list
     * @return
     */
    List<Dict> findDictByType(List<String> list);

    int updateStatusById(Dict dict);

    /**
     * 修改字典名称
     * @param dict
     * @return
     */
    int updateDictLabel(Dict dict);

    /**
     * 产品工厂定制方法
     * @param param
     * @return
     */
    HashMap productDict(Map param);

    /**
     * 产品工厂定制方法
     * @param param
     * @return
     */
    HashMap getProductDictValue(Map param);

    /**
     * 产品工厂定制方法-查询属性类型为字典的属性所存的字典项
     * @param map
     * @return
     */
    List<HashMap> getProDictValue(Map map);

    /**
     * 查询符合字典值
     * @param dict
     * @return
     */
    Dict selectDictByObj(Dict dict);
}