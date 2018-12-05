package com.jidong.productselection.enums;

import lombok.Getter;

/**
 * @Auther: LiuChong
 * @Date: 2018/10/23 14:17
 * @Description:
 */
@Getter
@Deprecated
public enum ProductTypeEnum {

	ROBOT_GRINDING_MACHINE(1,"机器人用修磨机"),

	ROBOT_GRINDING_REPLACING_INTEGRATED_MACHINE(2,"机器人用修磨更换一体机"),

	ROBOT_REPLACING_MACHINE(3,"机器人用更换机"),

	ROBOT_GRINDING_REPLACING_COMBINE_MACHINE(4,"机器人用修磨更换组合机");


	private Integer code;

	private String message;

	ProductTypeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
