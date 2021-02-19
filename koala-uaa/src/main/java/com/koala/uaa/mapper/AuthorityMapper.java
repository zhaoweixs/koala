package com.koala.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.koala.uaa.entity.AuthorityInfo;
import com.koala.uaa.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 11:28
 */
public interface AuthorityMapper extends BaseMapper<AuthorityInfo> {

    List<AuthorityInfo> findAuthorityByUserId(@Param("userId") Long userId);

}
