package com.koala.uaa.entity;

import lombok.Data;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 11:03
 */
@Data
public class UserInfo {

    private Long id;

    private Integer enabled;

    private String name;

    private String password;

    private Integer version;

}
