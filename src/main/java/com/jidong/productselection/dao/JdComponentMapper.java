package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.request.ComponentSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator 2018/11/02
 */
@Mapper
public interface JdComponentMapper {
	int deleteByPrimaryKey(Integer componentId);

	int insert(JdComponent record);

	int insertSelective(JdComponent record);

	JdComponent selectByPrimaryKey(Integer componentId);

	int updateByPrimaryKeySelective(JdComponent record);

	int updateByPrimaryKey(JdComponent record);

	List<JdComponent> findByComponentIdIn(@Param("componentIdList") List<Integer> componentIdList);

	Integer findMaxComponentId();

	List<JdComponent> findByLastCategoryIdAndComponentId(@Param("lastCategoryId") Integer lastCategoryId, @Param("componentId") Integer componentId);

	List<JdComponent> findByLastCategoryId(@Param("lastCategoryId") Integer lastCategoryId);

	List<JdComponent> findByComponentSearchRequest(@Param("searchRequest") ComponentSearchRequest searchRequest);

	int updateComponentTypeByComponentId(@Param("updatedComponentType") Integer updatedComponentType, @Param("componentId") Integer componentId);

	int updateIsDeletedByComponentId(@Param("updatedIsDeleted") Boolean updatedIsDeleted, @Param("componentId") Integer componentId);

	List<JdComponent> findByFirstCategoryId(@Param("firstCategoryId") Integer firstCategoryId);

	List<JdComponent> findByProductIdAndcomponentModelNumberOrComponentShortNumber(@Param("productId") Integer productId, @Param("componentModelNumber") String componentModelNumber, @Param("componentShortNumber") String componentShortNumber);

	List<JdComponent> findByLastCategoryIdIn(@Param("lastCategoryIdList")List<Integer> lastCategoryIdList);



}