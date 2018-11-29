package com.jidong.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2018/11/29
*/
@Data
public class JdUser {
    private Integer userId;

    private String username;

    private String chineseName;

    private String password;

    private String roles;

    private Boolean enable;
}