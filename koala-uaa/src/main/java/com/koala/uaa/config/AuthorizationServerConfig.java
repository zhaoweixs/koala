package com.koala.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 11:47
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }

    @Autowired
    private UserDetailsService jdbcUserDetailServiceImpl;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        security.tokenKeyAccess("isAuthenticated()");
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).userDetailsService(jdbcUserDetailServiceImpl).tokenStore(redisTokenStore());
    }

//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("permitAll()");  // 获取 token 的策略
//        security.checkTokenAccess("isAuthenticated()");
//    }
//
//    /**
//     * 配置客户端信息
//     *
//     * @param clients
//     * @throws Exception
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.withClientDetails(jdbcClientDetailsService());  //设置客户端的配置从数据库中读取，存储在oauth_client_details表
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager) // 开启密码验证，来源于 WebSecurityConfigurerAdapter
//                .userDetailsService(jdbcUserDetailServiceImpl) // 读取验证用户的信息
//                .tokenStore(redisTokenStore());

//    }

    @Bean
    public TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }
}
