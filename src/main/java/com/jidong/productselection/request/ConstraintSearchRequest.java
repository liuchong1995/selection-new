package com.jidong.productselection.request;

import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/21 11:25
 * @Description:
 */
@Data
public class ConstraintSearchRequest {

	private int page;

	private int rows;

	private Integer productId;

	private List<Integer> categoryIds;

	private Integer componentId;
}
