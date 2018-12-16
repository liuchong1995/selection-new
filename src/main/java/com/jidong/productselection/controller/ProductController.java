package com.jidong.productselection.controller;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdProduct;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/16 11:38
 * @Description:
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

	@Autowired
	private JdProductService productService;

	@GetMapping("/all")
	public BaseResponse<List<JdProduct>> findAll() {
		try {
			List<JdProduct> productList = productService.findAll();
			return BaseResponse.success(productList);
		} catch (Exception e) {
			log.error("获取产品列表错误！" + e.getMessage(), e);
			return BaseResponse.error("获取产品列表错误！");
		}
	}

	@GetMapping("/{prdId}")
	public BaseResponse<JdProduct> findOne(@PathVariable("prdId") Integer prdId) {
		try {
			JdProduct product = productService.findOne(prdId);
			return BaseResponse.success(product);
		} catch (Exception e) {
			log.error("获取产品错误！" + e.getMessage(), e);
			return BaseResponse.error("获取产品错误！");
		}
	}

	@GetMapping("/canBeDelete/{prdId}")
	public BaseResponse<List<JdCategory>> canBeDelete(@PathVariable("prdId") Integer prdId) {
		try {
			return BaseResponse.success(productService.canBeDelete(prdId));
		} catch (Exception e) {
			log.error("获取分类列表错误！" + e.getMessage(), e);
			return BaseResponse.error("获取分类列表错误！");
		}
	}

	@DeleteMapping("/{prdId}")
	public BaseResponse<Integer> deleteOne(@PathVariable("prdId") Integer prdId) {
		try {
			return BaseResponse.success(productService.delete(prdId));
		} catch (Exception e) {
			log.error("删除产品错误！" + e.getMessage(), e);
			return BaseResponse.error("删除产品错误！");
		}
	}

	@PostMapping("/")
	public BaseResponse<Integer> addProduct(@RequestBody JdProduct product) {
		try {
			return BaseResponse.success(productService.addProduct(product));
		} catch (Exception e) {
			log.error("增加产品错误！" + e.getMessage(), e);
			return BaseResponse.error("增加产品错误！");
		}
	}
}
