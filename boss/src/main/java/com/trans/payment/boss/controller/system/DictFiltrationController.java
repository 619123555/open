package com.trans.payment.boss.controller.system;

import com.alibaba.fastjson.JSON;
import com.trans.payment.boss.controller.BaseController;
import com.trans.payment.boss.entity.Organization;
import com.trans.payment.boss.service.system.OrganizationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/filtration/")
public class DictFiltrationController extends BaseController {

    @Resource
    private OrganizationService organizationService;

    /**
     * 查询一级机构合集 API接口 原接口 新增接口
     * @return
     */
    @PostMapping(value = "getOrgs")
    public String getOrgs() {
        List<Organization> orgList = organizationService.findOrgList("1");
        Map map = new HashMap<>(16);
        map.put("data", orgList);
        map.put("status", "200");
        return JSON.toJSONString(map);
    }
}
