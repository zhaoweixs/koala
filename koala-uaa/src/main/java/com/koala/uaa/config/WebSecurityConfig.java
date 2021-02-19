package com.koala.uaa.config;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 11:40
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService jdbcUserDetailServiceImpl;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jdbcUserDetailServiceImpl).passwordEncoder(passwordEncoder());
    }

    /**
     * 用于启用密码模式 否则密码模式无法使用
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().anyRequest()
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/login/*").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/test/**").authenticated();
//        http
//                .authorizeRequests()
//                .antMatchers("/**").authenticated()
//                .antMatchers("/test/hello").permitAll()
//                .antMatchers("/test/**").authenticated()
//                .and()
//                .userDetailsService(jdbcUserDetailServiceImpl);
    }
}
