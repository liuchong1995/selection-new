package com.jidong.productselection.controller;

import com.jidong.productselection.dto.ShelfHeight;
import com.jidong.productselection.entity.JdBracketMountingHeight;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdShelfConstraint;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdShelfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Auther: LiuChong
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
}
