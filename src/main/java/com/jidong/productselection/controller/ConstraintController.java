package com.jidong.productselection.controller;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.entity.JdMutexDescribe;
import com.jidong.productselection.entity.JdShelfConstraint;
import com.jidong.productselection.request.ConstraintRequest;
import com.jidong.productselection.request.ConstraintSearchRequest;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdConstraintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/10/25 15:00
 * @Description:
 */
@RestController
@RequestMapping("/constraint")
@Slf4j
public class ConstraintController {

	@Autowired
	private JdConstraintService constraintService;

	@PostMapping("/")
	public BaseResponse insertConstraint(@RequestBody ConstraintRequest constraintRequest){
		try {
			return BaseResponse.success(constraintService.insertConstraint(constraintRequest));
		} catch (Exception e) {
			log.error("增加约束错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}

	@DeleteMapping("/{constraintId}")
	public BaseResponse deleteConstraint(@PathVariable("constraintId") Integer constraintId){
		try {
			return BaseResponse.success(constraintService.deleteConstraint(constraintId));
		} catch (Exception e) {
			log.error("删除约束错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}

	@PostMapping("/deleteAll")
	public BaseResponse deleteConstraints(@RequestBody List<Integer> constraintIds){
		try {
			constraintService.deleteConstraints(constraintIds);
			return BaseResponse.success();
		} catch (Exception e) {
			log.error("批量删除约束错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}

	@PostMapping("/shelfConstraint")
	public BaseResponse deleteConstraints(@RequestBody JdShelfConstraint shelfConstraint){
		try {
			int n = constraintService.addShelfConstraint(shelfConstraint);
			return BaseResponse.success(n);
		} catch (Exception e) {
			log.error("增加架子约束错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}

	@GetMapping("/maxGroupId")
	public BaseResponse<Integer> getMaxGroupId(){
		try {
			return BaseResponse.success(constraintService.getMaxGroupId());
		} catch (Exception e) {
			log.error("获取最大组号错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}

	@PostMapping("/search")
	public BaseResponse<PageInfo<JdMutexDescribe>> search(
			@RequestBody ConstraintSearchRequest searchRequest
	){
		try {
			return BaseResponse.success(constraintService.search(searchRequest));
		} catch (Exception e) {
			log.error("获取约束列表错误错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}
}
