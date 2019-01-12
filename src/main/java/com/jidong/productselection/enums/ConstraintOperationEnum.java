package com.jidong.productselection.enums;

import lombok.Getter;

/**
 * @Author: LiuChong
 * @Date: 2018/10/19 14:21
 * @Description:
 */
@Getter
public enum ConstraintOperationEnum {

	ONLY_BE_USED(1, "仅可用于"),

	MUTEX(2, "互斥于"),

	ATTACHMENT(3, "附件"),

	MANDATORY(4, "必选"),

	SHELF_CONSTRAINT(5, "架子约束"),

	ADVANCE_MANDATORY(6, "高级必选约束");

	private Integer code;

	private String message;

	ConstraintOperationEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
