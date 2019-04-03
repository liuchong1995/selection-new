package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdProduct;
import org.apache.ibatis.annotations.Mapper;import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2019/4/3 13:01
 * @Description:
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

	Integer findNextProductId();
}