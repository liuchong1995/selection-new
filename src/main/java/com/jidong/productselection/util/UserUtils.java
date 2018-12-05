package com.jidong.productselection.util;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/29 16:58
 * @Description:
 */
public class UserUtils {
	public static String getCurrentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
