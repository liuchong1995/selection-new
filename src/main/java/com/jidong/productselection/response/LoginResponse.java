package com.jidong.productselection.response;

/**
 * @Author: LiuChong
 * @Date: 2018/12/3 15:43
 * @Description:
 */
public class LoginResponse {
	private Integer code;
	private String msg;
	private String token;

	private LoginResponse() {
	}

	public static LoginResponse build() {
		return new LoginResponse();
	}

	public static LoginResponse ok(String msg, String token) {
		return new LoginResponse(200, msg, token);
	}

	public static LoginResponse ok(String msg) {
		return new LoginResponse(200, msg, null);
	}

	public static LoginResponse error(String msg, String token) {
		return new LoginResponse(500, msg, token);
	}

	public static LoginResponse error(String msg) {
		return new LoginResponse(500, msg, null);
	}

	private LoginResponse(Integer code, String msg, String token) {
		this.code = code;
		this.msg = msg;
		this.token = token;
	}

	public Integer getCode() {
		return code;
	}

	public LoginResponse setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public LoginResponse setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public String getToken() {
		return token;
	}

	public LoginResponse setObj(String token) {
		this.token = token;
		return this;
	}
}
