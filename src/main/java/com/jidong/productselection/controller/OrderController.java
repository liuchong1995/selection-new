package com.jidong.productselection.controller;

import com.jidong.productselection.entity.JdOrder;
import com.jidong.productselection.request.GenerateOrderModelNumberRequest;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: LiuChong
 * @Date: 2018/12/6 22:42
 * @Description:
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

	@Autowired
	private JdOrderService orderService;

	@PostMapping("/modelNumber")
	public BaseResponse<String> generateModelNumber(@RequestBody GenerateOrderModelNumberRequest modelNumberRequest){
		try {
			String modelNumber = orderService.generateOrderModelNumber(modelNumberRequest);
			return BaseResponse.success("success",modelNumber);
		} catch (Exception e) {
			log.error("获取模型号错误！" + e.getMessage(), e);
			return BaseResponse.error("获取模型号错误！");
		}
	}

	@PostMapping("/getMandatoryResult")
	public BaseResponse getMandatoryResult(@RequestBody JdOrder order){
		try {
			return BaseResponse.success(orderService.getMandatoryResult(order));
		} catch (Exception e) {
			log.error("获取必选项错误！" + e.getMessage(), e);
			return BaseResponse.error("获取必选项错误！");
		}
	}

	@PostMapping("/add")
	public BaseResponse insert(@RequestBody JdOrder order){
		try {
			int n = orderService.insert(order);
			return BaseResponse.success(n);
		} catch (Exception e) {
			log.error("保存订单错误！" + e.getMessage(), e);
			return BaseResponse.error("保存订单错误！");
		}
	}
}
