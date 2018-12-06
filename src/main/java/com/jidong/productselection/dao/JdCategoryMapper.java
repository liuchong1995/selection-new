package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Mybatis Generator 2018/11/22
 */
@Mapper
public interface JdCategoryMapper {
	int deleteByPrimaryKey(Integer categoryId);

	int insert(JdCategory record);

	int insertSelective(JdCategory record);

	JdCategory selectByPrimaryKey(Integer categoryId);

	int updateByPrimaryKeySelective(JdCategory record);

	int updateByPrimaryKey(JdCategory record);

	List<Map<Object, Object>> getMenuTree(@Param("prdId") Integer prdId);

	List<JdCategory> findByCategoryIdIn(@Param("categoryIdList") List<Integer> categoryIdList);

	List<JdCategory> findByProductId(@Param("productId") Integer productId);

	List<JdCategory> findByProductIdAndParentIdAndCategoryId(@Param("productId") Integer productId, @Param("parentId") Integer parentId, @Param("categoryId") Integer categoryId);

	List<JdCategory> findByProductIdAndParentId(@Param("productId") Integer productId, @Param("parentId") Integer parentId);

	Integer findNextCategoryId();

	Integer findNextCategoryOrder();

	List<JdCategory> findByParentId(@Param("parentId") Integer parentId);

    List<JdCategory> getNewMenuTree(@Param("prdId") Integer prdId,@Param("parentId") Integer parentId);

    List<JdCategory> selectCateChildren(@Param("parentId") Integer parentId);
}