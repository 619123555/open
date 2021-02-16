package com.trans.payment.boss.controller.system;

import com.trans.payment.boss.commons.Result;
import com.trans.payment.boss.controller.BaseController;
import com.trans.payment.boss.entity.Organization;
import com.trans.payment.boss.entity.OrganizationNode;
import com.trans.payment.boss.mapper.OrganizationMapper;
import com.trans.payment.boss.service.system.OrganizationService;
import com.trans.payment.boss.utils.ConfUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/organization"
)
public class OrganizationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);


    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "/getTreeData")
    public Result getTreeData(
            @RequestParam(required = false) String extId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String parentId,
            @RequestParam(required = false) Long grade,
            @RequestParam(required = false) Boolean isAll,
            HttpServletResponse response) {

        List<Organization> mapList = new ArrayList<>();
        List<Organization> list = organizationMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            Organization e = list.get(i);
            if (!"0".equals(e.getParentId())
                    && (StringUtils.isBlank(extId)
                    || (StringUtils.isNotBlank(extId)
                    && !extId.equals(e.getId())
                    && e.getParentIds().indexOf("," + extId + ",") == -1))
                    && (StringUtils.isBlank(type)
                    || (StringUtils.isNotBlank(type) && type.equals(e.getType())))
                    && (grade == null
                    || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
                    && (StringUtils.isBlank(parentId)
                    || (StringUtils.isNotBlank(parentId)
                    && StringUtils.contains(e.getParentIds(), "," + parentId + ",")))) {
                mapList.add(e);
            }
        }

        // 获取根节点
        String rootOrgNode = ConfUtils.getConfValue("common_org");
        if (parentId != null) {
            rootOrgNode = parentId;
        }
        OrganizationNode orgNode = getOrgTreeString(rootOrgNode, mapList);
        // 循环遍历，获取层级
        return ok(orgNode.getChildren());
    }

    public static OrganizationNode getOrgTreeString(String id, List<Organization> orgList) {
        // 在当前用户菜单权限范围内查询
        OrganizationNode orgNode = getOrgObj(id, orgList);
        List<OrganizationNode> childOrgNodes = getOrgList(id, orgList);
        List<OrganizationNode> orgListNodes = new ArrayList<>();
        // 遍历子节点
        if (childOrgNodes != null && childOrgNodes.size() > 0) {
            for (int i = 0; i < childOrgNodes.size(); i++) {
                OrganizationNode on = childOrgNodes.get(i);
                if (StringUtils.isNotBlank(on.getOrgId())) {
                    OrganizationNode o = getOrgTreeString(on.getOrgId(), orgList);
                    orgListNodes.add(o);
                }
            }
        }
        if (orgListNodes.size() > 0) {
            orgNode.setChildren(orgListNodes);
        }
        return orgNode;
    }

    public static OrganizationNode getOrgObj(String id, List<Organization> orgList) {
        OrganizationNode orgNode = new OrganizationNode();
        for (Organization o : orgList) {
            if (id.equals(o.getId())) {
                orgNode.setParentId(o.getParentId());
                orgNode.setMaster(o.getMaster());
                orgNode.setOrgCode(o.getCode());
                orgNode.setOrgId(o.getId());
                orgNode.setOrgName(o.getName());
                orgNode.setOrgType(o.getType());
                orgNode.setKey(o.getId());
                orgNode.setParentIds(o.getParentIds());
                orgNode.setGrade(o.getGrade());
                orgNode.setSimpleCode(o.getSimpleCode());
                break;
            }
        }
        return orgNode;
    }

    public static List<OrganizationNode> getOrgList(String pid, List<Organization> orgList) {
        List<OrganizationNode> mls = new ArrayList<>();
        for (Organization o : orgList) {
            if (pid.equals(o.getParentId())) {
                OrganizationNode orgNode = new OrganizationNode();
                orgNode.setParentId(o.getParentId());
                orgNode.setMaster(o.getMaster());
                orgNode.setOrgCode(o.getCode());
                orgNode.setOrgId(o.getId());
                orgNode.setOrgName(o.getName());
                orgNode.setOrgType(o.getType());
                orgNode.setKey(o.getId());
                orgNode.setParentIds(o.getParentIds());
                orgNode.setGrade(o.getGrade());
                orgNode.setSimpleCode(o.getSimpleCode());
                mls.add(orgNode);
            }
        }
        return mls;
    }

    /**
     * 机构管理列表查询 api
     * @param organization organization
     * @return CommonResult
     */
    @PostMapping(value = "showOrgListData")
    public Result showOrgListData(Organization organization) {
        List<Organization> list = organizationService.findListByUser();
        // 获取根节点
        String rootOrgNode = ConfUtils.getConfValue("common_org");
        OrganizationNode orgNode = getOrgTreeString(rootOrgNode, list);
        String[] selectedKeys = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            selectedKeys[i] = list.get(i).getId();
        }
        try {
            Map<String, Object> map = new HashMap<>(16);
            map.put("data", orgNode.getChildren());
            map.put("selectedKeys", selectedKeys);
            map.put("pId", orgNode.getChildren() == null ? "2" : orgNode.getChildren().get(0).getOrgId());
            // 循环遍历，获取层级
            return successResult(map);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
        }
        return errorResult("系统异常");
    }

    @RequestMapping(value = "delOrgData", method = RequestMethod.POST)
    public Result delOrgData(Organization organization) {
        String[] ids = organization.getIds();
        for (int i = 0; i < ids.length; i++) {
            organizationMapper.deleteByPrimaryKey(ids[i]);
        }
        return ok("删除机构成功");
    }

    @RequestMapping(value = "saveOrgData", method = RequestMethod.POST)
    public Result saveOrgData(Organization organization) {
        try {
            //	organization=URLdecodeObj(organization);
            // 机构简称唯一
            if (organizationService.getOrgBySimpleCode(organization.getSimpleCode()) != null) {
                return error("机构简称已存在");
            }
            if (organizationService.getOrgByNo(organization.getCode()) != null) {
                return error("机构编码已存在");
            }
            if (organization.getParent() == null
                || "0".equals(organization.getParent().getId())
                || "".equals(organization.getParent().getId())) {
                String rootOrgNode = ConfUtils.getConfValue("common_org");
                organization.setParentId(rootOrgNode);
                organization.getParent().setId(rootOrgNode);
            }
            organization.setStatus("1");
            Organization newOrgParent =
                    organizationService.getOrganization(organization.getParent().getId());
            organization.setGrade(String.valueOf(Integer.valueOf(newOrgParent.getGrade()) + 1));
            organization.setParentId(newOrgParent.getId());
            // 获取排序号，最末节点排序号+30
            int sort = organizationService.maxSortInt(organization.getParentId()) + 30;
            organization.setSort(Long.valueOf(sort));
            organization.setParentIds(newOrgParent.getParentIds() + organization.getParentId() + ",");
            // 新增机构方法
            int i = organizationService.save(organization);
            return ok("新增机构成功");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            return error("新增机构失败");
        }
    }

    /**
     * 修改机构排序
     * @param id 机构id
     * @param type up向上移 down向下移
     * @return
     */
    @RequestMapping(value = "updateOrgSort")
    public Result updateOrgSort(String id, String type) {
        Organization org = organizationMapper.selectByPrimaryKey(id);
        if (org == null) {
            return error("修改失败");
        }
        // 查询下一个修改的机构
        Organization nextOrg = organizationService.getNextOrganization(org, type);
        if (nextOrg != null) {
            // 修改两个机构的排序
            organizationService.swopOrganizationSort(org, nextOrg);
        }
        return ok();
    }

    @RequestMapping(value = "updateOrgData")
    public Result updateOrgData(Organization organization) {
        Organization org = organizationMapper.selectByPrimaryKey(organization.getId());
        org.setName(organization.getName());
        org.setType(organization.getType());
        org.setMaster(organization.getMaster());
        organizationMapper.updateByPrimaryKeySelective(org);
        return ok("保存成功");
    }
}
