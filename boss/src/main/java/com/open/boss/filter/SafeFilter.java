package com.open.boss.filter;

import com.open.boss.entity.User;
import com.open.boss.utils.UserUtils;
import com.open.common.exception.PreException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SafeFilter extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SafeFilter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        User user = UserUtils.getUser();
        if (user == null || StringUtils.isEmpty(user.getId())) {
            logger.info("登录会话已过期");
            throw new PreException("登录会话已过期", 401);
        }
        return super.preHandle(request, response, handler);
    }
}
