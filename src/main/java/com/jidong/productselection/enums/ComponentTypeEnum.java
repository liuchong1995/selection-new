package com.jidong.productselection.enums;

import lombok.Getter;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/4 20:08
 * @Description:
 */
@Getter
public enum ComponentTypeEnum {

	VIRTUAL_COMPONENT(0,"非具体组件"),

	REALLY_COMPONENT(1,"具体组件"),

	ATTACHMENT(2,"附件"),

	SHELF(3,"架子"),

	INSTALLATION(4,"安装方式");

	private Integer code;

	private String message;

	ComponentTypeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
