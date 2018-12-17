package com.jidong.productselection.service;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.entity.JdRole;
import com.jidong.productselection.entity.JdUser;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/11/6 10:14
 * @Description:
 */
public interface JdUserService {
	PageInfo<JdUser> findAll(int page, int rows);

	List<JdRole> getAllRoles();

	int saveUser(JdUser user);

	Boolean hasSameUser(String username);

	int banOrDebanUser(Integer userId);

	int updatePassword(int userId, String newPassword);

	int updateRole(int userId, String newRoles);

	int deleteUser(Integer userId);

	JdUser getCurrentUser(String username);

    List<JdUser> getAllUserName();
}
