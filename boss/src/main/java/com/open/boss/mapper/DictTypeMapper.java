package com.open.boss.mapper;

import com.open.boss.entity.DictType;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface DictTypeMapper extends Mapper<DictType> {

    int enable(DictType dictType);

    DictType getByDictType(DictType dictType);

    int updateDictVersion(@Param("value") String value);

    List<DictType> getDictAllList();
}
