package com.jidong.productselection.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jidong.productselection.dao.JdRoleMapper;
import com.jidong.productselection.dao.JdUserMapper;
import com.jidong.productselection.entity.JdRole;
import com.jidong.productselection.entity.JdUser;
import com.jidong.productselection.service.JdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/11/6 10:14
 * @Description:
 */
@Service
public class JdUserServiceImpl implements JdUserService {

	@Autowired
	private JdUserMapper userMapper;

	@Autowired
	private JdRoleMapper roleMapper;

	@Override
	public PageInfo<JdUser> findAll(int page, int rows) {
		PageHelper.startPage(page, rows);
		List<JdUser> userList = userMapper.findAll();
		return new PageInfo<>(userList);
	}

	@Override
	public List<JdRole> getAllRoles() {
		return roleMapper.find();
	}

	@Override
	public int saveUser(JdUser user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setEnable(true);
		user.setUserId(userMapper.findNextUserId());
		return userMapper.insert(user);
	}

	@Override
	public Boolean hasSameUser(String username) {
		return userMapper.findByUsername(username) != null ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	@Transactional
	public int banOrDebanUser(Integer userId) {
		JdUser user = userMapper.selectByPrimaryKey(userId);
		user.setEnable(!user.getEnable());
		return userMapper.updateByPrimaryKey(user);
	}

	@Override
	@Transactional
	public int updatePassword(int userId, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(newPassword);
		return userMapper.updatePasswordByUserId(password,userId);
	}

	@Override
	@Transactional
	public int updatePassword(String username, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(newPassword);
		return userMapper.updatePasswordByUsername(password,username);
	}

	@Override
	@Transactional
	public int updateRole(int userId, String newRoles) {
		return userMapper.updateRolesBYUserId(newRoles,userId);
	}

	@Override
	@Transactional
	public int deleteUser(Integer userId) {
		return userMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public JdUser getCurrentUser(String username) {
		JdUser user = userMapper.findByUsername(username);
		user.setPassword("");
		return user;
	}

    @Override
    public List<JdUser> getAllUserName() {
        List<JdUser> userList = userMapper.findByEnable(Boolean.TRUE);
        userList.forEach(user -> user.setPassword(null));
        return userList;
    }

	@Override
	public Boolean checkOldPassword(String username, String password) {
		JdUser user = userMapper.findByUsername(username);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(password,user.getPassword());
	}
}
