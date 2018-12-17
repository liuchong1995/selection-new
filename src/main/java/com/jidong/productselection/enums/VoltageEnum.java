package com.jidong.productselection.enums;

import lombok.Getter;

/**
 * @Author: LiuChong
 * @Date: 2018/11/4 20:08
 * @Description:
 */
@Getter
public enum VoltageEnum {

	TWO_HUNDRED(200,"两百伏电压"),

	FOUR_HUNDRED(400,"四百伏电压"),

	FOUR_HUNDRED_EIGHTY(480,"四百八十伏电压");

	private Integer code;

	private String message;

	VoltageEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
