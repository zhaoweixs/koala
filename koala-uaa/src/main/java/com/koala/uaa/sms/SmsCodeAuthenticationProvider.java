package com.koala.uaa.sms;

import com.koala.uaa.service.JdbcUserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/20 10:27
 */
@AllArgsConstructor
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService jdbcUserDetailServiceImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken)authentication;

        UserDetails userDetails = ((JdbcUserDetailServiceImpl)jdbcUserDetailServiceImpl).loadUserByMobile(smsCodeAuthenticationToken.getPrincipal().toString());

        if (userDetails == null){
            throw new InternalAuthenticationServiceException("手机号输入错误");
        }

        SmsCodeAuthenticationToken smsCodeAuthenticationTokenResult = new SmsCodeAuthenticationToken(userDetails,userDetails.getAuthorities());
        smsCodeAuthenticationTokenResult.setDetails(smsCodeAuthenticationToken.getDetails());
        return smsCodeAuthenticationTokenResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
