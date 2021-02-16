package com.trans.payment.boss.configuration.shiro;

import com.trans.payment.common.constants.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ShiroConfiguration {

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(Constants.HASH_ALGORITHM);
        credentialsMatcher.setHashIterations(Constants.HASH_INTERATIONS);
        return credentialsMatcher;
    }

    @Bean("authRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public AuthShiroRealm authRealm() {
        AuthShiroRealm authRealm = new AuthShiroRealm();
        authRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        authRealm.setCachingEnabled(true);
        authRealm.setAuthorizationCachingEnabled(true);
        authRealm.setAuthenticationCachingEnabled(true);
        return authRealm;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(
            @Qualifier("authShiroRealm") AuthShiroRealm authShiroRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authShiroRealm);
        manager.setCacheManager(ehCacheManager());
        manager.setRememberMeManager(rememberMeManager());
        return manager;
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return cacheManager;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(
            @Qualifier("securityManager") DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 设置securityManager
        bean.setSecurityManager(manager);
        // 设置登录页面
        // 可以写路由也可以写jsp页面的访问路径
        bean.setLoginUrl("/login");
        // 设置登录成功跳转的页面
        bean.setSuccessUrl("/index");
        // 设置未授权跳转的页面
        bean.setUnauthorizedUrl("/index");
        // 定义过滤器
        LinkedHashMap<String, String> chains = new LinkedHashMap<>();
        chains.put("/index", "anon");
        chains.put("/article/**", "anon");
        chains.put("/login", "anon");
        chains.put("/static/css/**", "anon");
        chains.put("/static/js/**", "anon");
        chains.put("/static/fonts/**", "anon");
        chains.put("/static/font/**", "anon");
        chains.put("/static/images/**", "anon");
        bean.setFilterChainDefinitionMap(chains);
        return bean;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        return cookie;
    }

    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();
        listeners.add(sessionListener());
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(rememberMeCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(ehCacheManager());
        sessionManager.setGlobalSessionTimeout(-1);
        return sessionManager;
    }

    @Bean("sessionListener")
    public ShiroSessionListener sessionListener() {
        return new ShiroSessionListener();
    }

    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("session-user");
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new UidSessionIdGenerator();
    }
}
