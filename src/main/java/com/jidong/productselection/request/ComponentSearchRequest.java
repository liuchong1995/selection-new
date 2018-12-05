package com.jidong.productselection.request;


import lombok.Data;

/**
 * @Auther: LiuChong
 * @Date: 2018/10/31 10:56
 * @Description:
 */
@Data
public class ComponentSearchRequest {
	private Integer productId;

	private Integer firstCategoryId;

	private Integer secondCategoryId;

	private String componentModelNumber;

	private int page;

	private int rows;
}
