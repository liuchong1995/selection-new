package com.jidong.productselection.util;

import com.alibaba.fastjson.JSON;
import com.jidong.productselection.dao.JdOrderMapper;
import com.jidong.productselection.dto.CADResult;
import com.jidong.productselection.enums.OrderStatusEnum;
import com.jidong.productselection.service.JdOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class CADResultReceiver {

	@Autowired
	private SimpMessagingTemplate messageTemplate;

	@Autowired
	JdOrderService orderService;

	@Autowired
	JdOrderMapper orderMapper;

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${ps-connect-cad.cadtops-queue}",durable = "true"),
			exchange = @Exchange(name="${ps-connect-cad.exchange}",durable = "true",type = "topic"),
			key = "${ps-connect-cad.cadtops-queue}"
	)
	)
	@RabbitHandler
	public void process(Channel channel, Message message) throws IOException {
		byte[] body = message.getBody();
		String msg = new String(body, StandardCharsets.UTF_8);
		System.out.println("Receiver收到:" + msg +"   收到时间: "+new Date());
		try {
			//告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
			System.out.println("receiver success");
			CADResult cadResult = JSON.parseObject(msg, CADResult.class);
			if (Objects.equals(cadResult.getCode(), OrderStatusEnum.GENERATING.getCode())){
				orderService.changeOrderStatus(OrderStatusEnum.GENERATING,cadResult.getOrderId());
			} else if (Objects.equals(cadResult.getCode(), OrderStatusEnum.GENERATE_SUCCESS.getCode())){
				orderService.changeOrderStatus(OrderStatusEnum.GENERATE_SUCCESS,cadResult.getOrderId());
			} else if (Objects.equals(cadResult.getCode(), OrderStatusEnum.GENERATE_FAILURE.getCode())){
				orderService.changeOrderStatus(OrderStatusEnum.GENERATE_FAILURE,cadResult.getOrderId());
			}
			orderService.changeOrderMsg(cadResult.getMessage(), cadResult.getOrderId());
			cadResult.setOrder(orderMapper.selectByPrimaryKey(cadResult.getOrderId()));
			messageTemplate.convertAndSend("/topic/cadres", JSON.toJSONString(cadResult));
			log.info(JSON.toJSONString(cadResult));
			log.info("推送消息给前端完成");
		} catch (IOException e) {
			e.printStackTrace();
			//丢弃这条消息
			//channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
			System.out.println("receiver fail");
		}

	}
}

