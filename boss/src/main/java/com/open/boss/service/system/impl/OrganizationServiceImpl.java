package com.open.boss.service.system.impl;

import com.open.boss.mapper.OrganizationMapper;
import com.open.boss.service.system.OrganizationService;
import com.open.boss.entity.Organization;
import com.open.boss.entity.User;
import com.open.boss.utils.UserUtils;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<Organization> findListByUser() {
        User user = UserUtils.getUser();
        Organization org = UserUtils.getUserOrganization();
        Organization organization = new Organization();
        // 超级管理员
        logger.info("机构类型[{}]", org.getType());
        if ("1".equals(org.getType())) {
            organization.setParentIds("0," + "%");
        } else { // 只显示当前渠道
            organization.setParentIds("%" + user.getCompanyId() + ",%");
        }
        return organizationMapper.findByParentIdsLike(organization);
    }

    @Override
    public String maxIdStr() {
        Organization organization = organizationMapper.getOrgMaxID();
        int IntMaxOrgID = organization.getMaxId() + 1;
        return String.valueOf(IntMaxOrgID);
    }

    @Override
    public Organization getOrgBySimpleCode(String SimpleCode) {
        return organizationMapper.getOrgBySimpleCode(SimpleCode);
    }

    @Override
    public Organization getOrgByNo(String orgCode) {
        Organization organization = new Organization();
        organization.setCode(orgCode);
        organization = organizationMapper.getOrgByNo(organization);
        return organization;
    }

    @Override
    public Organization getOrganization(String id) {
        return organizationMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer maxSortInt(String parentId) {
        Integer maxSort = organizationMapper.getMaxSortByParentId(parentId);
        if (maxSort == null) {
            maxSort = 0;
        }
        return (maxSort + 1);
    }

    @Override
    public int save(Organization organization) {
        organization.setId(maxIdStr());
        organization.setCreateTime(new Date());
        organization.setOperTime(new Date());
        String userId = UserUtils.getUser().getId();
        organization.setCreator(userId);
        organization.setOperator(userId);
        return organizationMapper.insertSelective(organization);
    }

    @Override
    public Organization getNextOrganization(Organization organization, String type) {
        String orderBy = "desc";
        String opt = "<";
        if ("down".equals(type)) {
            orderBy = "asc";
            opt = ">";
        }
        return organizationMapper.getNextOrganization(
                organization.getSort().intValue(), organization.getParentId(), opt, orderBy);
    }

    @Override
    public void swopOrganizationSort(Organization org, Organization orgNext) {
        Long tempSort = org.getSort();
        org.setSort(orgNext.getSort());
        orgNext.setSort(tempSort);
        // 修改
        organizationMapper.updateOrganizationScot(org.getId(), org.getSort());
        organizationMapper.updateOrganizationScot(orgNext.getId(), orgNext.getSort());
    }

    @Override
    public List<Organization> findOrgList(String parentIDStr) {
        return organizationMapper.findOrgList(parentIDStr);
    }

    @Override
    public List<Organization> findByParentIdsLikeApi(String no) {
        User user = UserUtils.getUser(no);
        if (user == null) {
            return null;
        }
        Organization org = UserUtils.getUserOrganization(user);
        Organization organization = new Organization();
        // 超级管理员
        if ("1".equals(user.getId()) || "1".equals(org.getType())) {
            organization.setParentIds("0," + "%");
        } else {
            //只显示当前渠道
            organization.setParentIds("%" + user.getCompanyId() + ",%");
        }
        return organizationMapper.findByParentIdsLikeApi(organization);
    }

    @Override
    public Organization getOrganizationByCode(String code) {
        return organizationMapper.getOrganizationByCode(code);
    }

    @Override
    public int update(Organization o) {
        return organizationMapper.updateByPrimaryKeySelective(o);
    }

    @Override
    public List<Organization> findAll(Organization org) {
        return organizationMapper.select(org);
    }
}
