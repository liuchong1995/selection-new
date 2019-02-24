package com.jidong.productselection.service;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.request.CategoryAddRequest;

import java.util.List;
import java.util.Map;

/**
 * @Author: LiuChong
 * @Date: 2018/10/16 13:14
 * @Description:
 */
public interface JdCategoryService {

	@Deprecated
	String getMenuTree(Integer prdId);

	@Deprecated
	String getTreeList(List<Map<Object, Object>> resultMap);

	List<Integer> excludeAllCategory(JdCategory category);

	List<JdCategory> findByProductIdAndParentId(Integer productId, Integer parentId);

	JdCategory findOne(Integer categoryId);

	int add(CategoryAddRequest categoryAddRequest);

	int update(JdCategory category);

	int delete(JdCategory category);

	List<JdCategory> getAllLeafCategory(Integer categoryIds);

	List<JdCategory> getNewMenuTree(Integer prdId,Integer parentId);

	List<JdCategory> getAllNewMenuTree(Integer prdId,Integer parentId);

	List<JdCategory> getAllSubCates(Integer categoryId);

	Map<Integer, List<JdComponent>> partitionComp(List<JdComponent> jdComponents);

	Map<Integer, List<JdCategory>> partitionCate(List<JdCategory> jdCategories);

	List<List<JdComponent>> groupComp(Map<Integer, List<JdComponent>> compListMap);

	List<List<JdCategory>> groupCate(Map<Integer, List<JdCategory>> cateListMap);
}
