package com.jidong.productselection.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/30 10:38
 * @Description:
 */
@Data
public class OrderItemDetail {

	private Integer componentId;

	private Integer quantity;

	private List<Property> properties;

}
