package com.jidong.productselection.dao;
import com.jidong.productselection.entity.JdMandatory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator 2018/11/15
 */
@Mapper
public interface JdMandatoryMapper {
	int deleteByPrimaryKey(Integer mandatoryId);

	int insert(JdMandatory record);

	int insertSelective(JdMandatory record);

	JdMandatory selectByPrimaryKey(Integer mandatoryId);

	int updateByPrimaryKeySelective(JdMandatory record);

	int updateByPrimaryKey(JdMandatory record);

	Integer findNextMandatoryId();

	int updateIsDeletedBYMandatoryId(@Param("mandatoryId") Integer mandatoryId);

	List<JdMandatory> findByPremiseProductId(@Param("premiseProductId") Integer premiseProductId);

	List<JdMandatory> findByPremiseCategoryIdIn(@Param("premiseCategoryIdList") List<Integer> premiseCategoryIdList);

	List<JdMandatory> findByPremiseComponentIdIn(@Param("premiseComponentIdList") List<Integer> premiseComponentIdList);

}