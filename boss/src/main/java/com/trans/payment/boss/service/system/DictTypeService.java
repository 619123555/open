package com.trans.payment.boss.service.system;

import com.github.pagehelper.PageInfo;
import com.trans.payment.boss.entity.DictType;
import java.util.List;

public interface DictTypeService {

    List<DictType> findList(DictType dictType);

    void enable(DictType dictType);

    DictType getByDictType(DictType dictType);


    void save(DictType dictType);


    PageInfo<DictType> findDictPage(DictType dict, int pageNo, int pageSize);


    /**
     * 修改字典类型版本
     * @param value
     * @return
     */
    int updateDictVersion(String value);


    List<DictType> getDictAllList();


    int delete(DictType dictType);
}
