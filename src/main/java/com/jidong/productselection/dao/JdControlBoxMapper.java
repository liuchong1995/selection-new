package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdControlBox;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/22
*/
@Mapper
public interface JdControlBoxMapper {
    int deleteByPrimaryKey(Integer detailId);

    int insert(JdControlBox record);

    int insertSelective(JdControlBox record);

    JdControlBox selectByPrimaryKey(Integer detailId);

    int updateByPrimaryKeySelective(JdControlBox record);

    int updateByPrimaryKey(JdControlBox record);

    List<JdControlBox> find();

    JdControlBox findByComponentId(@Param("componentId") Integer componentId);
}