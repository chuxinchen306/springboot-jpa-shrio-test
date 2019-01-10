package hello.shrio;


import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


//import org.apache.shiro.cache.CacheManager;

/**
 * @author bootdo 1992lcg@163.com
 */
@Configuration
public class ShiroConfig {


    @Value("${server.session-timeout}")
    private int tomcatTimeout;

    @Value("${uncheck_urls}")
    private String uncheckUrls;

    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public FilterRegistrationBean securityFilterChain(AbstractShiroFilter securityFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(securityFilter);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.setName("shiroFilter");
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ERROR, DispatcherType.ASYNC, DispatcherType.INCLUDE);
        return registration;

    }


    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //鉴权走自定义filter，用于前后端分离
        //shiroFilterFactoryBean.setLoginUrl("/login");
        //shiroFilterFactoryBean.setSuccessUrl("/index");
        //shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //读取配置文件中的uncheckUrls
        for (String unchekcUrl : uncheckUrls.split(";")) {
            filterChainDefinitionMap.put(unchekcUrl, "anon");
        }
        filterChainDefinitionMap.put("/**", "authc");


//        LinkedHashMap<String, Filter> filtsMap = new LinkedHashMap<String, Filter>();
//        filtsMap.put("authc", new ShiroLoginFilter());
//        shiroFilterFactoryBean.setFilters(filtsMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionManager servletContainerSessionManager() {
        return new ServletContainerSessionManager();
    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheManager();
    }

    @Bean
    public MyModularRealmAuthenticator myModularRealmAuthenticator(){
        //设置realm.
        List<Realm> realms = new ArrayList<>();
        realms.add(userRealm());
        MyModularRealmAuthenticator myModularRealmAuthenticator = new MyModularRealmAuthenticator();
        myModularRealmAuthenticator.setRealms(realms);
        return myModularRealmAuthenticator;
    }

    /**
     *
     * @param realm 负责权限
     * @param servletContainerSessionManager session
     * @param cacheManager 缓存
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm realm,SessionManager servletContainerSessionManager, CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(myModularRealmAuthenticator());
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(servletContainerSessionManager);
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


}
