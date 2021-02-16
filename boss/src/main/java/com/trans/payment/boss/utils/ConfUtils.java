package com.trans.payment.boss.utils;

import com.trans.payment.boss.entity.Conf;
import com.trans.payment.boss.mapper.ConfMapper;
import com.trans.payment.common.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常量配置工具类
 */
public class ConfUtils {

    public static final String CONF_KEY = "q6n84c5h";
    private static ConfMapper confClient = SpringContextHolder.getBean(ConfMapper.class);
    private static Logger logger = LoggerFactory.getLogger(ConfUtils.class);


    /**
     * 通过全局域名取配置项的值（name包括key值和scope值）
     * @param confKey
     * @return
     */
    public static String getConfValue(String confKey) {
        Conf conf = new Conf();
        conf.setConfKey(confKey);
        conf = confClient.selectOne(conf);
        if (conf != null) {
            return DesUtils.getDESDecrypt(conf.getConfValue(), CONF_KEY);
        }
        return null;
    }
}