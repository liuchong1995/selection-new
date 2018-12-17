package com.jidong.productselection.enums;

import lombok.Getter;

/**
 * @Author: LiuChong
 * @Date: 2018/10/19 14:17
 * @Description: 感觉没办法重构了啊！！！！！
 */
@Getter
@Deprecated
public enum ConstraintTypeEnum {

	COMPONENT_TO_COMPONENT(1, "组件对组件"),

	COMPONENT_TO_CATEGORY(2, "组件对类型"),

	CATEGORY_TO_COMPONENT(3, "类型对组件"),

	CATEGORY_TO_CATEGORY(4, "类型对类型"),

	MIX_TO_COMPONENT(5, "混合对组件"),

	COMPONENT_TO_MIX(6, "组件对混合"),

	MIX_TO_CATEGORY(7, "混合对类型"),

	CATEGORY_TO_MIX(8, "类型对混合"),

	MIX_TO_MIX(9, "混合对混合");


	private Integer code;

	private String message;

	ConstraintTypeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
