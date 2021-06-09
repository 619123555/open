package com.open.boss.service.system;


import com.open.boss.service.BaseService;
import com.open.boss.entity.Conf;

public interface ConfService extends BaseService<Conf> {

    /**
     * 新增或保存配置信息
     * @param conf
     * @return int
     */
    int saveConf(Conf conf);
}
