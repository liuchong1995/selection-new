package com.jidong.productselection.dao;
import com.jidong.productselection.dto.ConstraintSearchCondition;
import com.jidong.productselection.entity.JdMutexDescribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator 2018/10/28
 */
@Mapper
public interface JdMutexDescribeMapper {
	int deleteByPrimaryKey(Integer describeId);

	int insert(JdMutexDescribe record);

	int insertSelective(JdMutexDescribe record);

	JdMutexDescribe selectByPrimaryKey(Integer describeId);

	int updateByPrimaryKeySelective(JdMutexDescribe record);

	int updateByPrimaryKey(JdMutexDescribe record);

	Integer findNextDescribeId();

	Integer findMaxGroupId();

	int deleteByDescribeId(@Param("describeId") Integer describeId);

	List<JdMutexDescribe> findByIsDeletedFalse();

	List<JdMutexDescribe> findByDescribeIdIn(@Param("describeIdList") List<Integer> describeIdList);

	void deleteByDescribeIdIn(@Param("describeIdList") List<Integer> describeIdList);

	List<JdMutexDescribe> findByProductId(@Param("productId") Integer productId);

	List<JdMutexDescribe> findByConstraintSearchCondition(@Param("searchCondition") ConstraintSearchCondition searchCondition);
}