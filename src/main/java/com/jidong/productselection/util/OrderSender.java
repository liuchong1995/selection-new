package com.jidong.productselection.util;

import com.jidong.productselection.entity.JdOrder;
import com.jidong.productselection.service.JdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: LiuChong
 * @Date: 2018/12/20 14:46
 * @Description:
 */
@Component
@Slf4j
public class OrderSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private JdOrderService orderService;

	//回调函数: confirm确认
	final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
		String messageId = correlationData.getId();
		if(ack){
			//如果confirm返回成功 则进行更新
			log.info("信息发送成功...");
			//orderService.changeOrderStatus(OrderStatusEnum.COMMITTED, Integer.valueOf(messageId));
		} else {
			//失败则进行具体的后续操作:重试 或者补偿等手段
			log.info("异常处理...");
		}
	};

	public void sendOrder(JdOrder order,String exchange,String routingKey) throws Exception {
		// 通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
		rabbitTemplate.setConfirmCallback(confirmCallback);
		//消息唯一ID
		CorrelationData correlationData = new CorrelationData(order.getOrderId().toString());
		rabbitTemplate.convertAndSend(exchange, routingKey, order.getOrderNumber(), correlationData);
	}
}
