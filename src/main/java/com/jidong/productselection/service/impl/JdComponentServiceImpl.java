package com.jidong.productselection.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jidong.productselection.dao.JdCategoryMapper;
import com.jidong.productselection.dao.JdComponentMapper;
import com.jidong.productselection.dao.JdProductMapper;
import com.jidong.productselection.dto.ComponentDetail;
import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdProduct;
import com.jidong.productselection.enums.ComponentTypeEnum;
import com.jidong.productselection.request.ComponentAddRequest;
import com.jidong.productselection.request.ComponentSearchRequest;
import com.jidong.productselection.service.JdCategoryService;
import com.jidong.productselection.service.JdComponentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: LiuChong
 * @Date: 2018/10/17 14:21
 * @Description:
 */
@Service
public class JdComponentServiceImpl implements JdComponentService {

	private static final Integer TOP_LEVEL = Integer.valueOf(0);

	@Value("${PRODUCT_IMG_REQUEST_PRE}")
	private String PRODUCT_IMG_REQUEST_PRE;

	@Autowired
	private JdCategoryMapper categoryMapper;

	@Autowired
	private JdComponentMapper componentMapper;

	@Autowired
	private JdProductMapper productMapper;

	@Autowired
	private JdCategoryService categoryService;

	@Override
	public List<JdComponent> findByCategory(Integer categoryId) {
		List<JdCategory> allLeafCategory = categoryService.getAllLeafCategory(categoryId);
		List<Integer> allComponentIds = new ArrayList<>();
		for (JdCategory category : allLeafCategory) {
			allComponentIds.addAll(JSON.parseArray(category.getComponentsId(),Integer.class));
		}
		if (allComponentIds.size() == 0){
			return new ArrayList<>();
		}
		List<JdComponent> componentList = componentMapper.findByComponentIdIn(allComponentIds);
		List<JdComponent> finalOptionList = componentList.stream().filter(comp -> !comp.getComponentType().equals(ComponentTypeEnum.ATTACHMENT.getCode())).collect(Collectors.toList());
		return finalOptionList;
	}

	@Override
	public JdComponent findById(Integer componentId) {
		return componentMapper.selectByPrimaryKey(componentId);
	}

	@Override
	public List<Integer> excludePeerComponent(JdComponent component, List<Integer> excludeAllCategoryIds) {
		List<JdComponent> excludeComponents = componentMapper.findByLastCategoryIdAndComponentId(component.getLastCategoryId(), component.getComponentId());
		List<Integer> excludePeerComponentIds = new ArrayList<>();
		for (JdComponent excludeComponent : excludeComponents) {
			excludePeerComponentIds.add(excludeComponent.getComponentId());
		}
		for (Integer excludeAllCategoryId : excludeAllCategoryIds) {
			JdCategory tempExcludeCategory = categoryMapper.selectByPrimaryKey(excludeAllCategoryId);
			if (tempExcludeCategory.getIsLeaf()){
				excludePeerComponentIds.addAll(JSON.parseArray(tempExcludeCategory.getComponentsId(),Integer.class));
			}
		}
		return excludePeerComponentIds;
	}

	@Override
	public List<JdComponent> findByLastCategoryId(Integer lastCategoryId) {
		return componentMapper.findByLastCategoryId(lastCategoryId);
	}

	@Override
	public PageInfo<JdComponent> search(ComponentSearchRequest searchRequest) {
		PageHelper.startPage(searchRequest.getPage(), searchRequest.getRows());
		List<JdComponent> componentList = componentMapper.findByComponentSearchRequest(searchRequest);
		PageInfo<JdComponent> componentPageInfo = new PageInfo<>(componentList);
		return componentPageInfo;
	}

	@Override
	public ComponentDetail findDetail(Integer componentId) {
		JdComponent component = componentMapper.selectByPrimaryKey(componentId);
		List<String> categoryNames = getCategoryList(component).stream().map(JdCategory::getCategoryName).collect(Collectors.toList());
		ComponentDetail componentDetail = new ComponentDetail();
		BeanUtils.copyProperties(component,componentDetail);
		componentDetail.setComponentImage(component.getComponentImage())
				.setCategoryNames(categoryNames)
				.setProductName(productMapper.selectByPrimaryKey(component.getProductId()).getProductName());
		return componentDetail;
	}

	@Override
	@Transactional
	public int insertComponent(ComponentAddRequest componentAddRequest) {
		JdComponent component = new JdComponent();
		BeanUtils.copyProperties(componentAddRequest,component);
		component.setComponentImage(PRODUCT_IMG_REQUEST_PRE + component.getComponentImage());
		component.setComponentId(componentMapper.findMaxComponentId() + 1);
		component.setFirstCategoryId(componentAddRequest.getCategoryIds().get(0));
		Integer lastCategoryId = componentAddRequest.getCategoryIds().get(componentAddRequest.getCategoryIds().size() - 1);
		JdCategory category = categoryMapper.selectByPrimaryKey(lastCategoryId);
		List<Integer> comps = JSON.parseArray(category.getComponentsId(), Integer.class);
		if (comps == null){
			comps = Collections.singletonList(component.getComponentId());
		} else {
			comps.add(component.getComponentId());
		}
		category.setComponentsId(JSON.toJSONString(comps));
		if (!category.getIsLeaf()){
			category.setIsLeaf(true);
		}
		categoryMapper.updateByPrimaryKeySelective(category);
		component.setLastCategoryId(lastCategoryId);
		Date now = new Date();
		component.setCreateTime(now);
		component.setUpdateTime(now);
		component.setCreator(componentAddRequest.getCreator());
		component.setIsDeleted(false);
		return componentMapper.insertSelective(component);
	}

	@Override
	@Transactional
	public int updateComponent(Integer componentId, ComponentAddRequest componentAddRequest) {
		JdComponent component = componentMapper.selectByPrimaryKey(componentId);
		BeanUtils.copyProperties(componentAddRequest,component);
		if (!component.getComponentImage().contains(PRODUCT_IMG_REQUEST_PRE)){
			component.setComponentImage(PRODUCT_IMG_REQUEST_PRE + component.getComponentImage());
		}
		component.setFirstCategoryId(componentAddRequest.getCategoryIds().get(0));
		Integer oldLastCategoryId = component.getLastCategoryId();
		Integer lastCategoryId = componentAddRequest.getCategoryIds().get(componentAddRequest.getCategoryIds().size() - 1);
		component.setLastCategoryId(lastCategoryId);
		//修改category表
		JdCategory oddCategory = categoryMapper.selectByPrimaryKey(oldLastCategoryId);
		List<Integer> toRemove = JSON.parseArray(oddCategory.getComponentsId(), Integer.class);
		toRemove.remove(componentId);
		oddCategory.setComponentsId(JSON.toJSONString(toRemove));
		categoryMapper.updateByPrimaryKeySelective(oddCategory);
		JdCategory newCategory = categoryMapper.selectByPrimaryKey(lastCategoryId);
		List<Integer> toAdd = JSON.parseArray(newCategory.getComponentsId(), Integer.class);
		if (toAdd == null){
			toAdd = new ArrayList<>();
		}
		toAdd.add(componentId);
		newCategory.setComponentsId(JSON.toJSONString(toAdd));
		categoryMapper.updateByPrimaryKeySelective(newCategory);

		Date now = new Date();
		component.setUpdateTime(now);
		return componentMapper.updateByPrimaryKey(component);
	}

	@Override
	@Transactional
	public int deleteComponent(Integer componentId) {
		JdComponent component = componentMapper.selectByPrimaryKey(componentId);
		JdCategory category = categoryMapper.selectByPrimaryKey(component.getLastCategoryId());
		if (!component.getIsDeleted()){
			List<Integer> list = JSON.parseArray(category.getComponentsId(), Integer.class);
			list.remove(componentId);
			if (list.size() == 0){
				category.setIsLeaf(Boolean.FALSE);
			}
			category.setComponentsId(JSON.toJSONString(list));
			categoryMapper.updateByPrimaryKeySelective(category);
			return componentMapper.updateIsDeletedByComponentId(Boolean.TRUE,componentId);
		} else {
			if (category.getIsLeaf()) {
				category.setComponentsId(JSON.toJSONString(Collections.singletonList(componentId)));
				category.setIsLeaf(false);
			} else {
				List<Integer> compIds = JSON.parseArray(category.getComponentsId(), Integer.class);
				compIds.add(componentId);
				category.setComponentsId(JSON.toJSONString(compIds));
			}
			return componentMapper.updateIsDeletedByComponentId(Boolean.FALSE,componentId);
		}
	}

	/**
	 * 获取一个部件的类型列表，从高到低
	 * @param component
	 * @return
	 */
	@Override
	public List<JdCategory> getCategoryList(JdComponent component){
		List<JdCategory> categoryList = new ArrayList<>();
		JdCategory category = categoryMapper.selectByPrimaryKey(component.getLastCategoryId());
		categoryList.add(category);
		while (!category.getParentId().equals(TOP_LEVEL)){
			category = categoryMapper.selectByPrimaryKey(category.getParentId());
			categoryList.add(category);
		}
		Collections.reverse(categoryList);
		return categoryList;
	}

	@Override
	public ComponentAddRequest getComponentToShow(Integer componentId) {
		ComponentAddRequest componentToShow = new ComponentAddRequest();
		JdComponent component = componentMapper.selectByPrimaryKey(componentId);
		BeanUtils.copyProperties(component,componentToShow);
		componentToShow.setCategoryIds(getCategoryList(component).stream().map(JdCategory::getCategoryId).collect(Collectors.toList()));
		return componentToShow;
	}

	@Override
	public List<JdComponent> findByFirstCategoryId(Integer productId) {
		JdProduct product = productMapper.selectByPrimaryKey(productId);
		return product.getInstallationId() != null ? componentMapper.findByFirstCategoryId(product.getInstallationId()) : Collections.emptyList();
	}

	@Override
	public List<JdComponent> isExit(ComponentAddRequest request) {
		return componentMapper.findByProductIdAndcomponentModelNumberOrComponentShortNumber(request.getProductId(),request.getComponentModelNumber(),request.getComponentShortNumber());
	}

}
