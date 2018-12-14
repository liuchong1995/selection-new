package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Mybatis Generator 2018/12/14
 */
@Mapper
public interface JdProductMapper {
	int deleteByPrimaryKey(Integer productId);

	int insert(JdProduct record);

	int insertSelective(JdProduct record);

	JdProduct selectByPrimaryKey(Integer productId);

	int updateByPrimaryKeySelective(JdProduct record);

	int updateByPrimaryKey(JdProduct record);

	List<JdProduct> findAll();
}