package com.jidong.productselection.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2018/11/16
*/
@Data
public class JdShelfConstraint {
    private Integer constraintId;

    private Integer productId;

    private Integer installation;

    private Integer relation;

    private Integer relationValue;
}