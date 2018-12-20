package com.jidong.productselection.util;

import com.jidong.productselection.dao.JdOrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: LiuChong
 * @Date: 2018/12/20 15:44
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderSenderTest {

	@Autowired OrderSender sender;

	@Value("${ps-connect-cad.exchange}")
	private String exchangeName;

	@Value("${ps-connect-cad.pstocad-queue}")
	private String queueName;

	@Autowired
	private JdOrderMapper orderMapper;

	@Test
	public void sendOrder() {
		try {
			sender.sendOrder(orderMapper.selectByPrimaryKey(25),exchangeName,queueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("发送完成");
	}
}