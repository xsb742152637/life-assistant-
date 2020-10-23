package com.xie.lifeassistant.util.config;

import com.xie.lifeassistant.util.shiro.AuthRealm;
import com.xie.lifeassistant.util.shiro.permission.DefaultPermissionFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Comment：
 * Created by IntelliJ IDEA.
 * User: xie
 * Date: 2020/9/28 16:29
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);


        DefaultPermissionFilter defaultPermissionFilter = new DefaultPermissionFilter();

        Map<String, Filter> cumstomfilterMap = new HashMap<>();
        //map里面key值要为authc才能使用自定义的过滤器
        cumstomfilterMap.put("defaultPermission", defaultPermissionFilter);

        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.html"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/theme/pc/login/index.html");
        // 设置无权限时跳转的 url;
        shiroFilterFactoryBean.setUnauthorizedUrl("/theme/pc/error/401.html");

        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //不拦截部分
        //filterChainDefinitionMap.put("/theme/pc/login/**", "anon");
        //filterChainDefinitionMap.put("/anon/**", "anon");
        //filterChainDefinitionMap.put("/public/**", "anon");
        filterChainDefinitionMap.put("/**", "anon");

        //其余接口一律拦截
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        //filterChainDefinitionMap.put("/**/*.html", "authc,defaultPermission");
        //filterChainDefinitionMap.put("/**/*.do", "authc,defaultPermission");
        //filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setFilters(cumstomfilterMap);
        System.out.println("Shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }

    /**
     * 注入 securityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    //注入权限管理
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * 自定义身份认证 realm;
     * <p>
     * 必须写这个类，并加上 @Bean 注解，目的是注入 CustomRealm，
     * 否则会影响 CustomRealm类 中其他类的依赖注入
     */
    @Bean
    public AuthRealm customRealm() {
        return new AuthRealm();
    }
}