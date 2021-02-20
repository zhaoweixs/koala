package com.koala.uaa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.koala.uaa.entity.AuthorityInfo;
import com.koala.uaa.entity.UserInfo;
import com.koala.uaa.mapper.AuthorityMapper;
import com.koala.uaa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 10:58
 */
@Service
public class JdbcUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserInfo::getName,username);
        UserInfo userInfo = userMapper.selectOne(lambdaQueryWrapper);

        List<AuthorityInfo> authorityInfoList = authorityMapper.findAuthorityByUserId(userInfo.getId());
        return new User(userInfo.getName(),userInfo.getPassword(),userInfo.getEnabled() == 1 ? true : false,true,true,true,authorityInfoList.stream().map(o->new SimpleGrantedAuthority(o.getAuthority())).collect(Collectors.toList()));
    }

    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserInfo::getMobile,mobile);
        UserInfo userInfo = userMapper.selectOne(lambdaQueryWrapper);

        List<AuthorityInfo> authorityInfoList = authorityMapper.findAuthorityByUserId(userInfo.getId());
        return new User(userInfo.getName(),userInfo.getPassword(),userInfo.getEnabled() == 1 ? true : false,true,true,true,authorityInfoList.stream().map(o->new SimpleGrantedAuthority(o.getAuthority())).collect(Collectors.toList()));
    }

}
