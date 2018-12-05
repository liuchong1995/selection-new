package com.jidong.productselection.response;

import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/12/3 15:56
 * @Description:
 */
@Data
public class UserInfo {
	private String name;

	private String avatar;

	private List<String> roles;
}
