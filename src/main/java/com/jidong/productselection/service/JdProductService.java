package com.jidong.productselection.service;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdProduct;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/16 11:00
 * @Description:
 */
public interface JdProductService {
	List<JdProduct> findAll();

	JdProduct findOne(Integer prdId);

	List<JdCategory> canBeDelete(Integer prdId);

	Integer delete(Integer prdId);

	Integer addProduct(JdProduct product);
}
