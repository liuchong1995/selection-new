package com.jidong.productselection.entity;

import lombok.Data;

/**
 * @Author: LiuChong
 * @Date: 2019/4/3 13:01
 * @Description:
 */
@Data
public class JdProduct {
	private Integer productId;

	private String productName;

	private String productImg;

	private Integer installationId;

	private Integer shelfId;

	private String segmentation;

	private Integer mainCateid;

	private Integer voltageId;

	private Boolean hasInstallation;

	private Boolean hasShelfheight;

	private Boolean hasMountedheight;
}