package com.jidong.service.impl;

import com.alibaba.fastjson.JSON;
import com.jidong.dao.JdRoleMapper;
import com.jidong.dao.JdUserMapper;
import com.jidong.entity.JdRole;
import com.jidong.entity.JdUser;
import com.jidong.response.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/29 16:40
 * @Description:
 */
@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private JdUserMapper userMapper;

	@Autowired
	private JdRoleMapper roleMapper;

	private static final String AVATAR = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		log.info("经过了UserDetailsServiceImpl");
		//构建角色列表
		List<GrantedAuthority> authorities = new ArrayList<>();
		//authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		//return new User("admin", "$2a$10$P4Jbrx5XKzy.6slbtWXM0.PKQuDdpo3FqPqZAvvnFzjvgrOKUEIo2", authorities);
		JdUser user = userMapper.findByUsername(s);
		if (user != null) {
			if (user.getEnable()) {
				List<Integer> roleIds = JSON.parseArray(user.getRoles(), Integer.class);
				List<JdRole> roles = roleMapper.findByRoleIdIn(roleIds);
				for (JdRole role : roles) {
					authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
				}
				return new User(s, user.getPassword(), authorities);
			} else {
				throw new LockedException("用户被禁用！");
			}
		} else {
			throw new UsernameNotFoundException("数据库中无此用户！");
		}
	}

	public UserInfo getUserInfo(String token){
		JdUser user = userMapper.findByUsername(token);
		List<Integer> roleIds = JSON.parseArray(user.getRoles(), Integer.class);
		List<String> roles = roleMapper.findByRoleIdIn(roleIds).stream().
				map(role -> role.getRoleName().split("_")[1].toLowerCase()).collect(Collectors.toList());
		UserInfo userInfo = new UserInfo();
		userInfo.setName(user.getUsername());
		userInfo.setAvatar(AVATAR);
		userInfo.setRoles(roles);
		return userInfo;
	}
}
