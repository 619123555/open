package com.trans.payment.boss.mapper;

import com.trans.payment.boss.entity.UserRole;
import com.trans.payment.boss.utils.InsertUidListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface UserRoleMapper extends Mapper<UserRole>, InsertUidListMapper<UserRole> {
}