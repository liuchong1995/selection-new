package com.jidong.productselection.controller;

import com.jidong.productselection.response.RespBean;
import com.jidong.productselection.response.UserInfo;
import com.jidong.productselection.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: LiuChong
 * @Date: 2018/12/3 15:55
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@GetMapping("/info")
	public RespBean getUserInfo(@RequestParam("token") String token){
		try {
			UserInfo userInfo = userService.getUserInfo(token);
			return RespBean.ok("获取用户信息成功",userInfo);
		} catch (Exception e) {
			log.error("获取用户信息失败" + e.getMessage(),e);
			return RespBean.error("获取用户信息失败");
		}
	}
}
