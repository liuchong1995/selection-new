package com.jidong.productselection.controller;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.entity.JdRole;
import com.jidong.productselection.entity.JdUser;
import com.jidong.productselection.request.UpdatePasswordRequest;
import com.jidong.productselection.request.UpdateRoleRequest;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.response.RespBean;
import com.jidong.productselection.response.UserInfo;
import com.jidong.productselection.service.JdUserService;
import com.jidong.productselection.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/12/3 15:55
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private JdUserService service;

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

	@GetMapping("/getAllUsers")
	public BaseResponse<List<JdUser>> getAllUsers(){
		try {
			return BaseResponse.success(service.getAllUserName());
		} catch (Exception e) {
			log.error("获取所有用户名错误！" + e.getMessage(), e);
			return BaseResponse.error("获取所有用户名错误！");
		}

	}

	@GetMapping("/roles")
	public BaseResponse<List<JdRole>> getAllRoles(){
		try {
			return BaseResponse.success(service.getAllRoles());
		} catch (Exception e) {
			log.error("获取角色列表错误错误！" + e.getMessage(), e);
			return BaseResponse.error("获取角色列表错误错误！");
		}
	}

	@GetMapping("/search")
	public BaseResponse<PageInfo<JdUser>> searchUsers(@RequestParam("page")int page, @RequestParam("rows")int rows){
		try {
			return BaseResponse.success(service.findAll(page,rows));
		} catch (Exception e) {
			log.error("获取用户列表错误错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}

	@PostMapping("/banOrDebanUser/{userId}")
	public BaseResponse banOrDebanUser(@PathVariable("userId") Integer userId){
		try {
			return BaseResponse.success(service.banOrDebanUser(userId));
		} catch (Exception e) {
			log.error("修改用户状态错误！" + e.getMessage(), e);
			return BaseResponse.error("修改用户状态错误！");
		}
	}

	@PostMapping("/updatePassword")
	public BaseResponse updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){
		try {
			return BaseResponse.success(service.updatePassword(updatePasswordRequest.getUserId(),updatePasswordRequest.getNewPassword()));
		} catch (Exception e) {
			log.error("修改用户密码错误！" + e.getMessage(), e);
			return BaseResponse.error("修改用户密码错误！");
		}
	}

	@PostMapping("/updateRole")
	public BaseResponse updateRole(@RequestBody UpdateRoleRequest updateRoleRequest){
		try {
			return BaseResponse.success(service.updateRole(updateRoleRequest.getUserId(),updateRoleRequest.getNewRoles()));
		} catch (Exception e) {
			log.error("修改用户角色错误！" + e.getMessage(), e);
			return BaseResponse.error("修改用户角色错误！");
		}
	}

	@DeleteMapping("/{userId}")
	public BaseResponse deleteUser(@PathVariable("userId") Integer userId){
		try {
			return BaseResponse.success(service.deleteUser(userId));
		} catch (Exception e) {
			log.error("删除用户错误！" + e.getMessage(), e);
			return BaseResponse.error("删除用户错误！");
		}
	}

	@PostMapping("/")
	public BaseResponse saveUser(@RequestBody JdUser user){
		try {
			return BaseResponse.success(service.saveUser(user));
		} catch (Exception e) {
			log.error("新增用户错误！" + e.getMessage(), e);
			return BaseResponse.error("新增用户错误！");
		}
	}
}
