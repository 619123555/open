package com.trans.payment.boss.service.system;

import com.github.pagehelper.PageInfo;
import com.trans.payment.boss.entity.Dict;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DictService {


    void save(Dict dict);


    void delete(Dict dict);


    void enable(Dict dict);

    /**
     * 根据机构过滤字典信息
     */
    PageInfo<Dict> findDictpage(Dict dict, int pageNo, int pageSize);

    /**
     * 查询全部字典项
     * @param dict
     * @return
     */
    List<Dict> findList(Dict dict);

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

    Dict selectDictByObj(Dict dict);
}