package com.jidong.productselection.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LiuChong
 * @Date: 2018/11/16 16:48
 * @Description:
 */
@Getter
public enum InstallationEnum {

	HORIZONTAL("H", "水平装"),

	VERTICAL("V", "垂直装"),

	PAPERBACK("F", "平装");

	private String code;

	private String message;

	InstallationEnum(String code,String message) {
		this.code = code;
		this.message = message;
	}
}
