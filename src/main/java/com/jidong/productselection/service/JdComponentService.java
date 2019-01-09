package com.jidong.productselection.service;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.dto.ComponentDetail;
import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.request.ComponentAddRequest;
import com.jidong.productselection.request.ComponentSearchRequest;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/17 14:21
 * @Description:
 */
public interface JdComponentService {
	List<JdComponent> findByCategory(Integer categoryId);

	List<JdComponent> findByCategoryIncludeAttachment(Integer categoryId);

	JdComponent findById(Integer componentId);

	List<Integer> excludePeerComponent(JdComponent component, List<Integer> excludeAllCategoryIds);

	List<JdComponent> findByLastCategoryId(Integer lastCategoryId);

	PageInfo<JdComponent> search(ComponentSearchRequest searchRequest);

	ComponentDetail findDetail(Integer componentId);

	int insertComponent(ComponentAddRequest componentAddRequest);

	int updateComponent(Integer componentId, ComponentAddRequest componentAddRequest);

	int deleteComponent(Integer componentId);

	List<JdCategory> getCategoryList(JdComponent component);

	ComponentAddRequest getComponentToShow(Integer componentId);

	List<JdComponent> findByFirstCategoryId(Integer productId);

	List<JdComponent> isExit(ComponentAddRequest request);
}
