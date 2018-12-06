package com.jidong.productselection.controller;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
