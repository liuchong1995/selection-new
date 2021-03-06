package com.jidong.productselection.service;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.dto.MandatoryResult;
import com.jidong.productselection.dto.OrderDetail;
import com.jidong.productselection.entity.JdOrder;
import com.jidong.productselection.enums.OrderStatusEnum;
import com.jidong.productselection.request.GenerateOrderModelNumberRequest;
import com.jidong.productselection.request.OrderSearchRequest;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/22 09:45
 * @Description:
 */
public interface JdOrderService {
	PageInfo<JdOrder> findByPage(int page, int rows);

	PageInfo<JdOrder> searchByPage(OrderSearchRequest orderSearchRequest);

	int deleteByOrderId(Integer orderId);

	JdOrder findOne(Integer orderId);

	int insert(JdOrder order);

	int update(JdOrder order);

	String generateOrderModelNumber(GenerateOrderModelNumberRequest modelNumberRequest);

	MandatoryResult getMandatoryResult(JdOrder order);

	OrderDetail getOrderDetail(Integer orderId,Boolean changeShelfCode);

    void commitPreview(Integer orderId);

    int changeOrderStatus(OrderStatusEnum orderStatusEnum, Integer orderId);

	int changeOrderMsg(String msg, Integer orderId);

	void waitForFinish(Integer orderId);

	int deleteList(List<Integer> orderIds);
}
