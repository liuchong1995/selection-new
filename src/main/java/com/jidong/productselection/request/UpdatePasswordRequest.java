package com.jidong.productselection.request;

import lombok.Data;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/6 15:26
 * @Description:
 */
@Data
public class UpdatePasswordRequest {

	private Integer userId;

	private String newPassword;
}
