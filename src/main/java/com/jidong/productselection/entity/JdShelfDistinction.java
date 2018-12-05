package com.jidong.productselection.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2018/11/22
*/
@Data
public class JdShelfDistinction {
    private Integer distinctionId;

    private String distinctionCode;

    private Integer productId;

    private String categoryIds;
}