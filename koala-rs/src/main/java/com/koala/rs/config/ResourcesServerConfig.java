package com.koala.rs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.sql.DataSource;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 14:39
 */
@Configuration
@EnableResourceServer
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("koala_resource").stateless(false);
        resources.tokenStore(redisTokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .logout()
                .logoutUrl("/logout")//虚拟的登出地址
                .logoutSuccessHandler(logoutSuccessHandler)//登出做的操作
                .and()
                .authorizeRequests()
                .antMatchers("/test/test").permitAll()
                .antMatchers("/test/**").authenticated();
    }
}
