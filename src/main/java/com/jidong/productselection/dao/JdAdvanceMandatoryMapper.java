package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdAdvanceMandatory;
import org.apache.ibatis.annotations.Mapper;

/**
* Created by Mybatis Generator 2019/01/12
*/
@Mapper
public interface JdAdvanceMandatoryMapper {
    int deleteByPrimaryKey(Integer mandatoryId);

    int insert(JdAdvanceMandatory record);

    int insertSelective(JdAdvanceMandatory record);

    JdAdvanceMandatory selectByPrimaryKey(Integer mandatoryId);

    int updateByPrimaryKeySelective(JdAdvanceMandatory record);

    int updateByPrimaryKey(JdAdvanceMandatory record);
}