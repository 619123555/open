package com.trans.payment.boss.controller.system;

import com.github.pagehelper.PageInfo;
import com.trans.payment.boss.commons.Result;
import com.trans.payment.boss.controller.BaseController;
import com.trans.payment.boss.entity.Conf;
import com.trans.payment.boss.service.system.ConfService;
import com.trans.payment.boss.utils.ConfUtils;
import com.trans.payment.boss.utils.DesUtils;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/conf"
)
public class ConfController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ConfController.class);

    @Resource
    private ConfService confService;


    @PostMapping(value = "showConfListData")
    public Result showConfListData(
            Conf conf,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<Conf> page = this.confService.selectEntityPage(conf, pageNo, pageSize, "id DESC");
        if (page != null && page.getList() != null) {
            for (Conf cf : page.getList()) {
                cf.setConfValue(DesUtils.getDESDecrypt(cf.getConfValue(), ConfUtils.CONF_KEY));
            }
        }
        return ok(page);
    }

    @PostMapping(value = "showConf")
    public Result showConf(Conf conf) {
        Conf confDetail = confService.selectByPrimaryKey(conf.getId());
        String name = confDetail.getConfKey();
        if (StringUtils.isNotBlank(name) && name.contains("_")) {
            conf.setConfKey(name.substring(name.indexOf('_') + 1));
        }
        return ok(conf);
    }

    @PostMapping(value = "save")
    public Result saveConf(Conf conf) {
        if (conf.getId() == null) {
            String key = conf.getScope() + "_" + conf.getConfKey();
            String value = ConfUtils.getConfValue(key);
            if (value != null) {
                return error("已存在常量配置项名为" + key + "的常量配置项");
            } else {
                this.confService.saveConf(conf);
                return ok("新增成功");
            }
        } else {
            String value = ConfUtils.getConfValue(conf.getConfKey());
            if (value != null && value.equals(conf.getConfValue())) {
                return error("已存在常量配置项名为" + conf.getConfKey() + "的常量配置项");
            } else {
                this.confService.saveConf(conf);
                return ok("修改成功");
            }
        }
    }

    @PostMapping(value = "delteConf")
    public Result enableConf(Conf conf) {
        if (conf.getId() == null) {
            return error("参数为空");
        }
        this.confService.deleteByPrimaryKey(conf);
        return ok();
    }
}
