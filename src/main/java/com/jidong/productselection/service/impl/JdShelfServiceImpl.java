package com.jidong.productselection.service.impl;

import com.alibaba.fastjson.JSON;
import com.jidong.productselection.dao.*;
import com.jidong.productselection.dto.ShelfHeight;
import com.jidong.productselection.entity.*;
import com.jidong.productselection.service.JdShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: LiuChong
 * @Date: 2018/11/16 22:27
 * @Description:
 */
@Service
public class JdShelfServiceImpl implements JdShelfService {
	
	@Autowired
	private JdComponentMapper componentMapper;

	@Autowired
	private JdBracketHeightMapper bracketHeightMapper;

	@Autowired
	private JdBracketMountingHeightMapper bracketMountingHeightMapper;

	@Autowired
	private JdShelfConstraintMapper shelfConstraintMapper;

	@Autowired
	private JdProductMapper productMapper;
	
	@Override
	public Map<Integer, List<ShelfHeight>> getAllShelfHeight(Integer productId) {
		JdProduct product = productMapper.selectByPrimaryKey(productId);
		Map<Integer, List<ShelfHeight>> map = new HashMap<>();
		if (product.getShelfId() != null){
			List<JdComponent> shelfList = componentMapper.findByFirstCategoryId(product.getShelfId());
			List<JdBracketHeight> bracketHeight = bracketHeightMapper.findByBracketIdIn(shelfList.stream().map(JdComponent::getComponentId).collect(Collectors.toList()));
			for (JdBracketHeight height : bracketHeight) {
				map.put(height.getBracketId(), JSON.parseArray(height.getHeights(), ShelfHeight.class));
			}
		}
		return map;
	}

	@Override
	public List<JdBracketMountingHeight> getAllMountHeight() {
		return bracketMountingHeightMapper.find();
	}

	@Override
	public List<JdShelfConstraint> getAllShelfConstraint(Integer productId) {
		return shelfConstraintMapper.findByProductId(productId);
	}

	@Override
	public List<JdComponent> getAllInstallation(Integer productId) {
		JdProduct product = productMapper.selectByPrimaryKey(productId);
		return product.getInstallationId() != null ? componentMapper.findByFirstCategoryId(product.getInstallationId()) : Collections.emptyList();
	}
}
