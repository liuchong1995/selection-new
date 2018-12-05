package com.jidong.productselection.dao;
import com.jidong.productselection.entity.JdShelfDistinction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/22
*/
@Mapper
public interface JdShelfDistinctionMapper {
    int deleteByPrimaryKey(Integer distinctionId);

    int insert(JdShelfDistinction record);

    int insertSelective(JdShelfDistinction record);

    JdShelfDistinction selectByPrimaryKey(Integer distinctionId);

    int updateByPrimaryKeySelective(JdShelfDistinction record);

    int updateByPrimaryKey(JdShelfDistinction record);

    List<JdShelfDistinction> findByProductId(@Param("productId") Integer productId);


}