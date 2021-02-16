package com.trans.payment.boss.service.system.impl;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trans.payment.boss.entity.Dict;
import com.trans.payment.boss.mapper.DictMapper;
import com.trans.payment.boss.service.system.DictService;
import com.trans.payment.boss.utils.UserUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictMapper dictMapper;

    @Override
    @Transactional(readOnly = false, rollbackFor = {Exception.class, RuntimeException.class})
    public void save(Dict dict) {
        if (dict.getDictType() != null) {
            dict.setLabel(dict.getDictType().getLabel());
            dict.setValue(dict.getDictType().getValue());
        }
        if (StringUtils.isEmpty(dict.getId())) {
            dict.setCreator(UserUtils.getUser().getId());
            dict.setCreateTime(new Date());
            dict.setOperator(UserUtils.getUser().getId());
            dict.setOperTime(new Date());
            dict.setId(IdUtil.simpleUUID());
            dictMapper.insertSelective(dict);
        } else {
            dict.setOperator(UserUtils.getUser().getId());
            dict.setOperTime(new Date());
            dictMapper.updateByPrimaryKeySelective(dict);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(Dict dict) {
        dictMapper.deleteByPrimaryKey(dict);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {Exception.class, RuntimeException.class})
    public void enable(Dict dict) {
        dictMapper.updateByPrimaryKeySelective(dict);
    }

    /**
     * 根据机构过滤字典信息
     */
    @Override
    public PageInfo<Dict> findDictpage(Dict dict, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize, "id desc");
        List<Dict> dicts = dictMapper.select(dict);
        PageInfo<Dict> pageInfo = new PageInfo<>(dicts);
        return pageInfo;
    }

    /**
     * 查询全部字典项
     * @param dict
     * @return
     */
    @Override
    public List<Dict> findList(Dict dict) {
        return dictMapper.findList(dict);
    }

    /**
     * 修改字典名称
     * @param dict
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = {Exception.class, RuntimeException.class})
    public int updateDictLabel(Dict dict) {
        return dictMapper.updateDictLabel(dict);
    }


    /**
     * 产品工厂定制方法
     * @param param
     * @return
     */
    @Override
    public HashMap productDict(Map param) {
        return dictMapper.productDict(param);
    }

    /**
     * 产品工厂定制方法
     * @param param
     * @return
     */
    @Override
    public HashMap getProductDictValue(Map param) {
        return dictMapper.getProductDictValue(param);
    }

    /**
     * 产品工厂定制方法-查询属性类型为字典的属性所存的字典项
     * @param map
     * @return
     */
    @Override
    public List<HashMap> getProDictValue(Map map) {
        return dictMapper.getProDictValue(map);
    }

    @Override
    public Dict selectDictByObj(Dict dict) {
        return dictMapper.selectDictByObj(dict);
    }
}
