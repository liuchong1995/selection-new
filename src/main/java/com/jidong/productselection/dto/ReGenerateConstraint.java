package com.jidong.productselection.dto;

import lombok.Data;

/**
 * @Author: LiuChong
 * @Date: 2018/12/29 21:57
 * @Description: 用于存储约束请求 在新增部件时重新生成仅可用于约束
 */
@Data
public class ReGenerateConstraint {

	private Integer productId;

	private ReConstraintPremise reConstraintPremise;

	private Integer constraintOperation;

	private ReConstraintResult reConstraintResult;

	private Integer constraintType;

	private Integer groupId;

	private String groupName;

	private String constraintDesc;
}
