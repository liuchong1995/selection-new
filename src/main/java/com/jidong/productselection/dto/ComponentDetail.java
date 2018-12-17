package com.jidong.productselection.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/11/8 15:42
 * @Description:
 */
@Data
@Accessors(chain = true)
public class ComponentDetail {
	private String componentName;

	private String componentReamrk;

	private String componentModelNumber;

	private String componentShortNumber;

	private String componentDetail;

	private List<String> categoryNames;

	private String componentImage;

	private String productName;

	private String componentKey;

	private Integer componentType;
}
