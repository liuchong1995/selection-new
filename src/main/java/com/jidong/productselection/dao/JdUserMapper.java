package com.jidong.productselection.dao;
import com.jidong.productselection.entity.JdUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/05
*/
@Mapper
public interface JdUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(JdUser record);

    int insertSelective(JdUser record);

    JdUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(JdUser record);

    int updateByPrimaryKey(JdUser record);

    JdUser findByUsername(@Param("username") String username);

    List<JdUser> findAll();

	List<JdUser> findByEnable(@Param("enable") Boolean enable);

	Integer findNextUserId();

	int updatePasswordByUserId(@Param("updatedPassword") String updatedPassword, @Param("userId") Integer userId);

	int updateRolesBYUserId(@Param("updatedRoles") String updatedRoles, @Param("userId") Integer userId);



}