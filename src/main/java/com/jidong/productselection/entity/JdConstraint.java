package com.jidong.productselection.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2018/10/19
*/
@Data
public class JdConstraint {
    private Integer constraintId;

    private Integer productId;

    private Integer constraintType;

    private Integer constraintOperation;

    private String constraintPremise;

    private String constraintResult;
}