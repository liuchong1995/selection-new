package com.jidong.productselection.util;

import com.alibaba.fastjson.JSON;
import com.jidong.productselection.dao.JdCategoryMapper;
import com.jidong.productselection.dao.JdComponentMapper;
import com.jidong.productselection.dto.*;
import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdMutexDescribe;
import com.jidong.productselection.request.ConstraintRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LiuChong
 * @Date: 2018/12/29 22:00
 * @Description:
 */
@Component
public class ConstraintUtil {

	private static final Integer TOP_LEVEL = 0;

	@Autowired
	private JdComponentMapper componentMapper;

	@Autowired
	private JdCategoryMapper categoryMapper;

	public ConstraintRequest convertToRequest(ReGenerateConstraint reGenerateConstraint) {
		ConstraintRequest constraintRequest = new ConstraintRequest();
		BeanUtils.copyProperties(reGenerateConstraint, constraintRequest);
		ReConstraintPremise reConstraintPremise = reGenerateConstraint.getReConstraintPremise();
		ReConstraintResult reConstraintResult = reGenerateConstraint.getReConstraintResult();
		ConstraintPremise constraintPremise = new ConstraintPremise(Collections.emptyList(), Collections.emptyList());
		ConstraintResult constraintResult = new ConstraintResult(Collections.emptyList(), Collections.emptyList());
		if (reConstraintPremise.getCategoryIds().size() > 0){
			constraintPremise.setCategories(categoryMapper.findByCategoryIdIn(reConstraintPremise.getCategoryIds()));
		}
		if (reConstraintPremise.getComponentIds().size() > 0){
			constraintPremise.setComponents(componentMapper.findByComponentIdIn(reConstraintPremise.getComponentIds()));
		}
		if (reConstraintResult.getCategoryIds().size() > 0){
			constraintResult.setCategories(categoryMapper.findByCategoryIdIn(reConstraintResult.getCategoryIds()));
		}
		if (reConstraintResult.getComponentIds().size() > 0){
			constraintResult.setComponents(componentMapper.findByComponentIdIn(reConstraintResult.getComponentIds()));
		}
		constraintRequest.setConstraintPremise(constraintPremise);
		constraintRequest.setConstraintResult(constraintResult);
		return constraintRequest;
	}

	public ReGenerateConstraint convertToStore(ConstraintRequest constraintRequest) {
		ReGenerateConstraint reGenerateConstraint = new ReGenerateConstraint();
		BeanUtils.copyProperties(constraintRequest, reGenerateConstraint);
		ConstraintPremise constraintPremise = constraintRequest.getConstraintPremise();
		ConstraintResult constraintResult = constraintRequest.getConstraintResult();
		ReConstraintPremise reConstraintPremise = new ReConstraintPremise(Collections.emptyList(), Collections.emptyList());
		ReConstraintResult reConstraintResult = new ReConstraintResult(Collections.emptyList(), Collections.emptyList());
		if (constraintPremise.getCategories().size() > 0) {
			reConstraintPremise.setCategoryIds(constraintPremise.getCategories().stream().map(JdCategory::getCategoryId).collect(Collectors.toList()));
		}
		if (constraintPremise.getComponents().size() > 0) {
			reConstraintPremise.setComponentIds(constraintPremise.getComponents().stream().map(JdComponent::getComponentId).collect(Collectors.toList()));
		}
		if (constraintResult.getCategories().size() > 0) {
			reConstraintResult.setCategoryIds(constraintResult.getCategories().stream().map(JdCategory::getCategoryId).collect(Collectors.toList()));
		}
		if (constraintResult.getComponents().size() > 0) {
			reConstraintResult.setComponentIds(constraintResult.getComponents().stream().map(JdComponent::getComponentId).collect(Collectors.toList()));
		}
		reGenerateConstraint.setReConstraintPremise(reConstraintPremise);
		reGenerateConstraint.setReConstraintResult(reConstraintResult);
		return reGenerateConstraint;
	}

	public boolean needReGenerate(JdComponent component, JdMutexDescribe mutexDescribe){
		ReGenerateConstraint reGenerateConstraint = JSON.parseObject(mutexDescribe.getRegenerateRequest(), ReGenerateConstraint.class);
		boolean result = false;
		if (StringUtils.hasText(mutexDescribe.getRegenerateRequest()) && reGenerateConstraint.getReConstraintResult().getComponentIds().size() > 0){
			List<JdComponent> components = componentMapper.findByComponentIdIn(reGenerateConstraint.getReConstraintResult().getComponentIds());
			Optional<JdComponent> first = components.stream().filter(ele -> ele.getLastCategoryId().equals(component.getLastCategoryId())).findFirst();
			if (first.isPresent()){
				result = true;
			}
		}
		return result;
	}

	public boolean needReGenerate(JdCategory category, JdMutexDescribe mutexDescribe){
		ReGenerateConstraint reGenerateConstraint = JSON.parseObject(mutexDescribe.getRegenerateRequest(), ReGenerateConstraint.class);
		boolean result = false;
		if (StringUtils.hasText(mutexDescribe.getRegenerateRequest()) && reGenerateConstraint.getReConstraintResult().getCategoryIds().size() > 0 && !category.getParentId().equals(TOP_LEVEL)){
			List<JdCategory> categories = categoryMapper.findByCategoryIdIn(reGenerateConstraint.getReConstraintResult().getCategoryIds());
			Optional<JdCategory> first = categories.stream().filter(ele -> ele.getParentId().equals(category.getParentId())).findFirst();
			if (first.isPresent()){
				result = true;
			}
		}
		return result;
	}
}
