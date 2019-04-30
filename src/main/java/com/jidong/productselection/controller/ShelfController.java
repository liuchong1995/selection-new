package com.jidong.productselection.controller;

import com.jidong.productselection.dto.ShelfHeight;
import com.jidong.productselection.entity.JdBracketHeight;
import com.jidong.productselection.entity.JdBracketMountingHeight;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdShelfConstraint;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdShelfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: LiuChong
 * @Date: 2018/11/16 22:39
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/shelf")
public class ShelfController {

	@Autowired
	private JdShelfService shelfService;

	@GetMapping("/getAllHeight/{prdId}")
	public BaseResponse<Map<Integer, List<ShelfHeight>>> getAllHeight(@PathVariable("prdId") Integer prdId){
		try {
			return BaseResponse.success(shelfService.getAllShelfHeight(prdId));
		} catch (Exception e){
			log.error("获取架子高度错误!", e);
			return BaseResponse.error("获取架子高度错误!");
		}
	}

	@GetMapping("/getMountHeight")
	public BaseResponse<List<JdBracketMountingHeight>> getMountHeight(){
		try {
			return BaseResponse.success(shelfService.getAllMountHeight());
		} catch (Exception e){
			log.error("获取安装高度错误！", e);
			return BaseResponse.error("获取安装高度错误！");
		}
	}

	@GetMapping("/getShelfConstraint/{prdId}")
	public BaseResponse<List<JdShelfConstraint>> getShelfConstraint(@PathVariable("prdId") Integer prdId){
		try {
			return BaseResponse.success(shelfService.getAllShelfConstraint(prdId));
		} catch (Exception e){
			log.error("获取架子约束错误！", e);
			return BaseResponse.error("获取架子约束错误！");
		}
	}

	@GetMapping("/getAllInstallation/{prdId}")
	public BaseResponse<List<JdComponent>> getAllInstallation(@PathVariable("prdId") Integer prdId){
		try {
			return BaseResponse.success(shelfService.getAllInstallation(prdId));
		} catch (Exception e){
			log.error("获取安装方式错误！", e);
			return BaseResponse.error("获取安装方式错误！");
		}
	}

	@GetMapping("/getConstraint/{shelfId}")
	public BaseResponse<List<ShelfHeight>> getConstraint(@PathVariable("shelfId") Integer shelfId){
		try {
			return BaseResponse.success(shelfService.getConstraint(shelfId));
		} catch (Exception e){
			log.error("获取架子约束错误!", e);
			return BaseResponse.error("获取架子约束错误!");
		}
	}

	@PostMapping("/save")
	public BaseResponse<Integer> save(@RequestBody JdBracketHeight bracketHeight) {
		try {
			return BaseResponse.success(shelfService.insertOrUpdate(bracketHeight.getHeights(),bracketHeight.getBracketId()));
		} catch (Exception e){
			log.error("保存架子约束错误!", e);
			return BaseResponse.error("保存架子约束错误!");
		}
	}
}
