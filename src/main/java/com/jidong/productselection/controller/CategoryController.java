package com.jidong.productselection.controller;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.request.RefactorTreeRequest;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdCategoryService;
import com.jidong.productselection.service.JdConstraintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: LiuChong
 * @date: 2018/12/5 22:59
 * @desciption:
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

	@Autowired
	private JdCategoryService categoryService;

	@Autowired
	private JdConstraintService constraintService;

	@GetMapping("/menu")
	public BaseResponse<List<JdCategory>> getNewMenu(
			@RequestParam("prdId") Integer prdId, @RequestParam("parentId") Integer parentId
	) {
		try {
			return BaseResponse.success(categoryService.getNewMenuTree(prdId, parentId));
		} catch (Exception e) {
			log.error("获取菜单树信息失败!" + e.getMessage(), e);
			return BaseResponse.error("获取分类树信息失败!");
		}
	}

	@PostMapping("/refactor")
	public BaseResponse<List<JdCategory>> refactorCategoryMenu(@RequestBody RefactorTreeRequest refactorTreeRequest) {
		try {
			List<JdCategory> newMenuTree = constraintService.refactorNewMenuTree(refactorTreeRequest.getProductId(), refactorTreeRequest.getSelectedList());
			return BaseResponse.success("success", newMenuTree);
		} catch (Exception e) {
			log.error("重建部件类型菜单错误！", e);
			return BaseResponse.error("重建部件类型菜单错误！");
		}
	}

	@GetMapping("/{categoryId}")
	public BaseResponse<JdCategory> findOne(@PathVariable("categoryId") Integer categoryId) {
		try {
			return BaseResponse.success(categoryService.findOne(categoryId));
		} catch (Exception e) {
			log.error("获取类型菜单错误！", e);
			return BaseResponse.error("获取类型菜单错误！");
		}
	}
}
