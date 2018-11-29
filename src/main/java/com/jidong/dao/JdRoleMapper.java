package com.jidong.dao;
import com.jidong.entity.JdRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/29
*/
@Mapper
public interface JdRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(JdRole record);

    int insertSelective(JdRole record);

    JdRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(JdRole record);

    int updateByPrimaryKey(JdRole record);

    List<JdRole> findByRoleIdIn(@Param("roleIdList")List<Integer> roleIdList);

}