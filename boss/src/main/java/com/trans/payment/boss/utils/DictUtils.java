package com.trans.payment.boss.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trans.payment.common.constants.Constants;
import com.trans.payment.boss.entity.Dict;
import com.trans.payment.boss.mapper.DictMapper;
import com.trans.payment.common.utils.SpringContextHolder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;

/**
 * 字典工具类
 */
public class DictUtils {

    private static DictMapper dictDao = SpringContextHolder.getBean(DictMapper.class);

    private static JedisCluster jedisCluster = SpringContextHolder.getBean(JedisCluster.class);

    public static Dict getDict(String type, String value) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            for (Dict dict : getDictList(type)) {
                if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
                    return dict;
                }
            }
        }
        return null;
    }

    public static String getDictLabel(String type, String value, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            for (Dict dict : getDictList(type)) {
                if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
                    return dict.getLabel();
                }
            }
        }
        return defaultValue;
    }

    public static String getDictLabels(String type, String values, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)) {
            List<String> valueList = new ArrayList<>();
            for (String value : StringUtils.split(values, ",")) {
                valueList.add(getDictLabel(value, type, defaultValue));
            }
            return StringUtils.join(valueList, ",");
        }
        return defaultValue;
    }

    public static String getDictValue(String type, String label, String defaultLabel) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
            for (Dict dict : getDictList(type)) {
                if (type.equals(dict.getType()) && label.equals(dict.getLabel())) {
                    return dict.getValue();
                }
            }
        }
        return defaultLabel;
    }

    public static List<Dict> getDictList(String type) {
        Dict d = new Dict();
        d.setType(type);
        List<Dict> dictList = dictDao.select(d);
        if (dictList == null) {
            dictList = new ArrayList<>();
        }
        return dictList;
    }

    public static List<Dict> getTkDictList(String type) {
        String key = Constants.CARD_SYS_DICT + type;
        String json = jedisCluster.get(key);
        List<Dict> dictList;
        if (StringUtils.isEmpty(json)) {
            Dict d = new Dict();
            d.setType(type);
            dictList = dictDao.select(d);
            if (dictList != null && dictList.size() > 0) {
                String content = JSON.toJSONString(dictList);
                jedisCluster.set(key, content);
            }
        } else {
            dictList = JSONObject.parseArray(json, Dict.class);
        }
        return dictList;
    }
}
