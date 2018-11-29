package com.jidong.util;

import com.jidong.entity.JdUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/29 16:58
 * @Description:
 */
public class UserUtils {
	public static JdUser getCurrentUser() {
		JdUser user = new JdUser();
		user.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return user;
	}
}
