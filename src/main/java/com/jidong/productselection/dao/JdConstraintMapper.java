package com.jidong.productselection.dao;
import com.jidong.productselection.entity.JdConstraint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/10/19
*/
@Mapper
public interface JdConstraintMapper {
    int deleteByPrimaryKey(Integer constraintId);

    int insert(JdConstraint record);

    int insertSelective(JdConstraint record);

    JdConstraint selectByPrimaryKey(Integer constraintId);

    int updateByPrimaryKeySelective(JdConstraint record);

    int updateByPrimaryKey(JdConstraint record);

    List<JdConstraint> findByConstraintType(@Param("constraintType") Integer constraintType);

}