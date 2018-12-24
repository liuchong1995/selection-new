package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: LiuChong
 * @Date: 2018/12/24 11:27
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JdOrderMapperTest {

	@Autowired
	private JdOrderMapper orderMapper;

	@Test
	public void test1() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse("2018-12-15");
		List<JdOrder> oneDayOrder = orderMapper.findOneDayOrder(date);
		System.out.println();
	}

}