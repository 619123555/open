package com.trans.payment.boss.service.system.impl;

import com.trans.payment.boss.entity.Conf;
import com.trans.payment.boss.service.impl.BaseServiceImpl;
import com.trans.payment.boss.service.system.ConfService;
import com.trans.payment.boss.utils.ConfUtils;
import com.trans.payment.boss.utils.DesUtils;
import com.trans.payment.boss.utils.UserUtils;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConfServiceImpl extends BaseServiceImpl<Conf> implements ConfService {
    private static final Logger logger = LoggerFactory.getLogger(ConfServiceImpl.class);

    @Override
    public int saveConf(Conf conf) {
        String key = conf.getScope() + "_" + conf.getConfKey();
        logger.debug(key);
        conf.setConfKey(key);
        String enValue = DesUtils.getDESEncrypt(conf.getConfValue(), ConfUtils.CONF_KEY);
        conf.setConfValue(enValue);
        conf.setCreator(UserUtils.getUser().getId());
        conf.setOperator(conf.getCreator());
        conf.setCreateTime(new Date());
        conf.setOperTime(new Date());
        conf.setStatus("1");
        if (conf.getId() != null) {
            return this.mapper.updateByPrimaryKeySelective(conf);
        } else {
            return this.mapper.insertSelective(conf);
        }
    }
}