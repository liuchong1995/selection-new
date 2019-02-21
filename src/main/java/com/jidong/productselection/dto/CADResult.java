package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdOrder;
import lombok.Data;

/**
 * @Author: LiuChong
 * @Date: 2019/2/20 10:05
 * @Description:
 */
@Data
public class CADResult {

	private Integer code;

	private String message;

	private Integer orderId;

	private JdOrder order;
}
