package com.open.boss.service.system;


import com.open.boss.entity.Organization;
import java.util.List;

public interface OrganizationService {

    List<Organization> findListByUser();

    String maxIdStr();


    Organization getOrgBySimpleCode(String SimpleCode);


    Organization getOrgByNo(String orgCode);


    Organization getOrganization(String id);


    /**
     * -- Tan 获取相同parentID下的部门的最大sort
     * @return
     */
    Integer maxSortInt(String parentId);


    int save(Organization organization);


    Organization getNextOrganization(Organization organization, String type);

    void swopOrganizationSort(Organization org, Organization orgNext);


    List<Organization> findOrgList(String parentIDStr);

    List<Organization> findByParentIdsLikeApi(String no);

    Organization getOrganizationByCode(String code);


    int update(Organization o);

    List<Organization> findAll(Organization org);
}
