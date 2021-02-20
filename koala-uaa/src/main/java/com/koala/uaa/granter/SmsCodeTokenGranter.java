package com.koala.uaa.granter;

import com.koala.uaa.sms.SmsCodeAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 20:09
 */
public class SmsCodeTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "sms";

    private RedisTemplate<String,Object> redisTemplate;

    private AuthenticationManager authenticationManager;

    public SmsCodeTokenGranter(AuthenticationManager authenticationManager,
                                  AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory, RedisTemplate<String,Object> redisTemplate) {
        this(authenticationManager,tokenServices,clientDetailsService,requestFactory,GRANT_TYPE);
        this.redisTemplate = redisTemplate;
    }

    protected SmsCodeTokenGranter(AuthenticationManager authenticationManager,
                                  AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }


    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");
        String code = parameters.get("code");

        if (StringUtils.isEmpty(code)){
            throw new UserDeniedAuthorizationException("请输入验证码");
        }

        Object codeCache = null;
        try {
            codeCache = redisTemplate.opsForValue().get("K_SMS_" + mobile);
        } catch (Exception e) {
            throw new UserDeniedAuthorizationException("获取验证码错误");
        }

        if (codeCache == null){
            throw new UserDeniedAuthorizationException("验证码不存在");
        }

        if (!StringUtils.equalsIgnoreCase(code,codeCache.toString())){
            throw new UserDeniedAuthorizationException("验证码输入错误");
        }

        redisTemplate.delete("K_SMS_" + mobile);

        Authentication authentication = new SmsCodeAuthenticationToken(mobile);
        ((AbstractAuthenticationToken)authentication).setDetails(parameters);

        try {
            authentication = authenticationManager.authenticate(authentication);
        } catch (AccountStatusException | BadCredentialsException e) {
            throw new InvalidGrantException(e.getMessage());
        }

        if (authentication == null || !authentication.isAuthenticated()){
            throw new InvalidGrantException("无法验证手机："+mobile);
        }

        OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(client,tokenRequest);
        return new OAuth2Authentication(oAuth2Request,authentication);
    }

}
