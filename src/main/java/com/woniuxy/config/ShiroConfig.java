package com.woniuxy.config;

import com.woniuxy.filter.JwtFilter;
import com.woniuxy.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //realm
    @Bean
    public Realm realm(){
        UserRealm userRealm = new UserRealm();
        /*HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);*/
        return userRealm;
    }
    //defaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm());
        defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager());
        return defaultWebSecurityManager;
    }
    //ShriofilterFactoryBeam
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());

        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        filterMap.put("jwt",new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);


        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        //白名单
        //linkedHashMap.put("/register.html","anon");
        linkedHashMap.put("/login/logout","logout");
        linkedHashMap.put("/user/login","anon");
        /*linkedHashMap.put("/user/register","anon");
        linkedHashMap.put("/js/**" ,"anon");
        linkedHashMap.put("/css/**","anon");*/
        //黑名单
        linkedHashMap.put("/**","jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(linkedHashMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMemanager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");//要给cookie取个名字
        simpleCookie.setMaxAge(7*24*60*60);
        cookieRememberMemanager.setCookie(simpleCookie);
        cookieRememberMemanager.setCipherKey(Base64.decode("a1b2c3d4e5f6g7h8i9j10l=="));
        return cookieRememberMemanager;
    }
}
