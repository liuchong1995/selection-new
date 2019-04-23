package com.jidong.productselection.request;

import lombok.Data;

import java.util.Date;

/**
 * @Author: LiuChong
 * @Date: 2018/10/22 09:52
 * @Description:
 */
@Data
public class OrderSearchRequest {
	private Integer productId;

	private Date startDate;

	private Date endDate;

	private String productModel;

	private String costumer;

	private String endUser;

	private String creator;

	private int page;

	private int rows;
}
