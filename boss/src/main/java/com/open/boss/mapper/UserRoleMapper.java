package com.open.boss.mapper;

import com.open.boss.utils.InsertUidListMapper;
import com.open.boss.entity.UserRole;
import tk.mybatis.mapper.common.Mapper;

public interface UserRoleMapper extends Mapper<UserRole>, InsertUidListMapper<UserRole> {
}
