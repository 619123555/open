package com.open.boss.service.system.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.boss.entity.DictType;
import com.open.boss.mapper.DictTypeMapper;
import com.open.boss.service.system.DictTypeService;
import com.open.boss.utils.UserUtils;
import com.open.common.constants.Constants;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Autowired
    private DictTypeMapper dictTypeDao;

    @Resource
    private JedisCluster jedisCluster;

    @Override
    public List<DictType> findList(DictType dictType) {
        return dictTypeDao.select(dictType);
    }

    @Override
    @Transactional(readOnly = false)
    public void enable(DictType dictType) {
        dictTypeDao.enable(dictType);
    }

    @Override
    public DictType getByDictType(DictType dictType) {
        String key = Constants.CARD_SYS_DICT_TYPE + dictType.getValue();
        DictType dt;
        String json = jedisCluster.get(key);
        if (StringUtils.isEmpty(json)) {
            dt = dictTypeDao.getByDictType(dictType);
            String content = JSON.toJSONString(dt);
            jedisCluster.set(key, content);
        } else {
            dt = JSONObject.parseObject(json, DictType.class);
        }
        return dt;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {Exception.class, RuntimeException.class})
    public void save(DictType dictType) {
        if (StringUtils.isEmpty(dictType.getId())) {
            dictType.setCreateTime(new Date());
            dictType.setId(IdUtil.simpleUUID());
            dictType.setCreator(UserUtils.getUser().getId());
            dictType.setOperTime(dictType.getCreateTime());
            dictType.setOperator(dictType.getCreator());
            dictTypeDao.insertSelective(dictType);
        } else {
            DictType d = dictTypeDao.selectByPrimaryKey(dictType.getId());
            d.setOperator(UserUtils.getUser().getId());
            d.setOperTime(new Date());
            d.setLabel(dictType.getLabel());
            d.setValue(dictType.getValue());
            d.setDescription(dictType.getDescription());
            d.setStatus(dictType.getStatus());
            d.setScope(dictType.getScope());
            dictTypeDao.updateByPrimaryKeySelective(d);
        }
    }


    @Override
    public PageInfo<DictType> findDictPage(DictType dict, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize, "id desc");
        List<DictType> dicts = dictTypeDao.select(dict);
        PageInfo<DictType> pageInfo = new PageInfo<>(dicts);
        return pageInfo;
    }

    @Override
    @Transactional(readOnly = false)
    public int updateDictVersion(String value) {
        return dictTypeDao.updateDictVersion(value);
    }


    @Override
    public List<DictType> getDictAllList() {
        return dictTypeDao.getDictAllList();
    }

    @Override
    public int delete(DictType dictType) {
        return dictTypeDao.deleteByPrimaryKey(dictType);
    }
}
