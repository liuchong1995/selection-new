package com.jidong.productselection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/7 10:26
 * @Description:
 */
@Data
@AllArgsConstructor
public class OrderItem {

	private Integer compId;

	private Integer qty;

	private Integer compType;

	private Integer compParent;

	private String compModelNum;
}
