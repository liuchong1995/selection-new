package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdMutex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator 2018/10/24
 */
@Mapper
public interface JdMutexMapper {
	int deleteByPrimaryKey(Integer mutexId);

	int insert(JdMutex record);

	int insertSelective(JdMutex record);

	JdMutex selectByPrimaryKey(Integer mutexId);

	int updateByPrimaryKeySelective(JdMutex record);

	int updateByPrimaryKey(JdMutex record);

	List<JdMutex> findByProductIdAndMutexType(@Param("productId") Integer productId, @Param("mutexType") Integer mutexType);

	List<JdMutex> findByMutexType(@Param("mutexType") Integer mutexType);

	Integer findNextMutexId();

}