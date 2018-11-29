package com.jidong.dao;
import com.jidong.entity.JdUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* Created by Mybatis Generator 2018/11/29
*/
@Mapper
public interface JdUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(JdUser record);

    int insertSelective(JdUser record);

    JdUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(JdUser record);

    int updateByPrimaryKey(JdUser record);

    JdUser findByUsername(@Param("username")String username);
}