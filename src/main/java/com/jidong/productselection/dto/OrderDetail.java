package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdOrder;
import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/26 17:13
 * @Description:
 */
@Data
public class OrderDetail {

	private JdOrder order;

	private List<JdComponent> components;
}
