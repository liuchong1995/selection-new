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
@Deprecated
public enum ProductInstallationEnum {

	ROBOT_GRINDING_MACHINE(1, 2, 5, "机器人用修磨机对应安装方式");

	private Integer productId;

	private Integer installationId;

	private Integer shelfId;

	private String message;

	private static final Map<Integer, ProductInstallationEnum> MAP = new HashMap<>();

	static {
		for (ProductInstallationEnum item : ProductInstallationEnum.values()) {
			MAP.put(item.productId, item);
		}
	}

	public static ProductInstallationEnum getByProductId(Integer productId) {
		return MAP.get(productId);
	}

	ProductInstallationEnum(Integer productId, Integer installationId, Integer shelfId,String message) {
		this.productId = productId;
		this.installationId = installationId;
		this.shelfId = shelfId;
		this.message = message;
	}
}
