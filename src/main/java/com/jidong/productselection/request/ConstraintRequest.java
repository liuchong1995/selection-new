package com.jidong.productselection.request;

import com.jidong.productselection.dto.ConstraintPremise;
import com.jidong.productselection.dto.ConstraintResult;
import lombok.Data;

/**
 * @Author: LiuChong
 * @Date: 2018/10/25 10:51
 * @Description:
 */
@Data
public class ConstraintRequest {

	private Integer productId;

	private ConstraintPremise constraintPremise;

	private Integer constraintOperation;

	private ConstraintResult constraintResult;

	private Integer constraintType;

	private Integer groupId;

	private String groupName;

	private String constraintDesc;
}
