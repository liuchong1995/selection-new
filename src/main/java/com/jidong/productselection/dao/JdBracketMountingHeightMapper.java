package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdBracketMountingHeight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/16
*/
@Mapper
public interface JdBracketMountingHeightMapper {
    int insert(JdBracketMountingHeight record);

    int insertSelective(JdBracketMountingHeight record);

    List<JdBracketMountingHeight> find();

	JdBracketMountingHeight findByHeight(@Param("height") Integer height);

}