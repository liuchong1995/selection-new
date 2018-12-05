package com.jidong.productselection.request;

import lombok.Data;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/6 15:25
 * @Description:
 */
@Data
public class UpdateRoleRequest {

	private Integer userId;

	private String newRoles;
}
