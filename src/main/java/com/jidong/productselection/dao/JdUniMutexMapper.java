package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdUniMutex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator 2018/10/28
 */
@Mapper
public interface JdUniMutexMapper {
	int deleteByPrimaryKey(Integer mutexId);

	int insert(JdUniMutex record);

	int insertSelective(JdUniMutex record);

	JdUniMutex selectByPrimaryKey(Integer mutexId);

	int updateByPrimaryKeySelective(JdUniMutex record);

	int updateByPrimaryKey(JdUniMutex record);

	List<JdUniMutex> findByProductId(@Param("productId") Integer productId);

	Integer findNextMutexId();

	int deleteByMutexIdIn(@Param("mutexIdList") List<Integer> mutexIdList);


}