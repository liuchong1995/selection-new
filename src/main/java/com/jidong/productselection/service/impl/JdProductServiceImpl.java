package com.jidong.productselection.service.impl;

import com.jidong.productselection.dao.JdCategoryMapper;
import com.jidong.productselection.dao.JdProductMapper;
import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdProduct;
import com.jidong.productselection.service.JdProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/16 11:00
 * @Description:
 */
@Service
public class JdProductServiceImpl implements JdProductService {

	@Value("${PRODUCT_IMG_REQUEST_PRE}")
	private String PRODUCT_IMG_REQUEST_PRE;

	@Autowired
	private JdProductMapper productMapper;

	@Autowired
	private JdCategoryMapper categoryMapper;

	@Override
	public List<JdProduct> findAll() {

		List<JdProduct> productList = productMapper.findAll();
		for (JdProduct jdProduct : productList) {
			jdProduct.setProductImg(PRODUCT_IMG_REQUEST_PRE + jdProduct.getProductImg());
		}
		return productList;
	}

	@Override
	public JdProduct findOne(Integer prdId) {
		JdProduct product = productMapper.selectByPrimaryKey(prdId);
		product.setProductImg(PRODUCT_IMG_REQUEST_PRE + product.getProductImg());
		return product;
	}

	@Override
	public List<JdCategory> canBeDelete(Integer prdId) {
		List<JdCategory> categories = categoryMapper.findByProductId(prdId);
		if (categories == null){
			return new ArrayList<>();
		}
		return categories;
	}

	@Override
	public Integer delete(Integer prdId) {
		return productMapper.deleteByPrimaryKey(prdId);
	}

	@Override
	public Integer addProduct(JdProduct product) {
		Integer productId = productMapper.findNextProductId();
		product.setProductId(productId);
		return productMapper.insert(product);
	}
}
