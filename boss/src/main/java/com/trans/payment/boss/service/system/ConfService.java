package com.trans.payment.boss.service.system;


import com.trans.payment.boss.entity.Conf;
import com.trans.payment.boss.service.BaseService;

public interface ConfService extends BaseService<Conf> {

    /**
     * 新增或保存配置信息
     * @param conf
     * @return int
     */
    int saveConf(Conf conf);
}
