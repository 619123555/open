package com.trans.payment.boss.mapper;

import com.trans.payment.boss.entity.Organization;
import com.trans.payment.boss.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Primary;
import tk.mybatis.mapper.common.Mapper;


public interface OrganizationMapper extends Mapper<Organization> {

    List<Organization> findByParentIdsLikeApi(Organization organization);

    List<Organization> findByParentIdsLike(Organization organization);

    Organization getOrgByNo(Organization organization);

    Organization getOrgMaxID();

    Organization getOrgBySimpleCode(@Param("simpleCode") String simpleCode);

    Integer getMaxSortByParentId(@Param("parentId") String parentId);

    Organization getNextOrganization(
        @Param("sort") int sort,
        @Param("parentId") String parentId,
        @Param("opt") String opt,
        @Param("sqlOrderBy") String sqlOrderBy);

    void updateOrganizationScot(@Param("id") String id, @Param("sort") Long sort);

    List<Organization> findOrgList(@Param("parentIDStr") String parentIDStr);


    Organization getOrganizationByCode(@Param("code") String code);

    List<Organization> findmanageChannelList(User user);
}