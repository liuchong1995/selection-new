package com.jidong.productselection.service;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdMutexDescribe;
import com.jidong.productselection.entity.JdShelfConstraint;
import com.jidong.productselection.request.ConstraintRequest;
import com.jidong.productselection.request.ConstraintSearchRequest;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/19 11:08
 * @Description:
 */
public interface JdConstraintService {

	String refactorMenuTree(Integer productId, List<JdComponent> selectedList);

	List<JdCategory> refactorNewMenuTree(Integer productId, List<JdComponent> selectedList);

	List<JdComponent> getOptionalListBySelected(Integer productId, List<JdComponent> selectedList);

	List<JdComponent> findOrderComponents(List<Integer> componentIds);

	int insertConstraint(ConstraintRequest constraintRequest);

	int deleteConstraint(Integer constraintId);

	Integer getMaxGroupId();

	PageInfo<JdMutexDescribe> findAll(int page, int rows);

	void deleteConstraints(List<Integer> constraintIds);

	int addShelfConstraint(JdShelfConstraint shelfConstraint);

	PageInfo<JdMutexDescribe> search(ConstraintSearchRequest searchRequest);

	List<JdCategory> getBanCategoryList(Integer productId, List<JdComponent> selectedComponents);

	List<JdComponent> getBanComponentList(Integer productId, List<JdComponent> selectedComponents);

	List<JdCategory> getCategories(List<JdComponent> selectedList);
}
