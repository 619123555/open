package com.open.boss.configuration.shiro;

import cn.hutool.core.util.IdUtil;
import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

public class UidSessionIdGenerator implements SessionIdGenerator {

    public UidSessionIdGenerator() {
    }

    @Override
    public Serializable generateId(Session session) {
        return IdUtil.simpleUUID();
    }
}
