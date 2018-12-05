package com.jidong.productselection.service;

import com.jidong.productselection.entity.JdProduct;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/16 11:00
 * @Description:
 */
public interface JdProductService {
	List<JdProduct> findAll();
}
