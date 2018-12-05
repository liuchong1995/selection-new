package com.jidong.productselection.dao;
import com.jidong.productselection.entity.JdShelfConstraint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/16
*/
@Mapper
public interface JdShelfConstraintMapper {
    int deleteByPrimaryKey(Integer constraintId);

    int insert(JdShelfConstraint record);

    int insertSelective(JdShelfConstraint record);

    JdShelfConstraint selectByPrimaryKey(Integer constraintId);

    int updateByPrimaryKeySelective(JdShelfConstraint record);

    int updateByPrimaryKey(JdShelfConstraint record);

    Integer findNestConstraintId();

    List<JdShelfConstraint> findByProductId(@Param("productId") Integer productId);


}