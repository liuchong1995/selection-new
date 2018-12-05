package com.jidong.productselection.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: LiuChong
 * @Date: 2018/10/16 11:03
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

	private static final int CODE_SUCCESS = 200;

	private static final int CODE_ERROR = 500;

	private int code;

	private String msg;

	private T data;


	public static <T> BaseResponse<T> success() {
		return new BaseResponse<T>(CODE_SUCCESS, "success", null);
	}

	public static <T> BaseResponse<T> success(String message) {
		return new BaseResponse<T>(CODE_SUCCESS, message, null);
	}

	public static <T> BaseResponse<T> success(T data) {
		return new BaseResponse<T>(CODE_SUCCESS, "success", data);
	}

	public static <T> BaseResponse<T> success(String message, T data) {
		return new BaseResponse<T>(CODE_SUCCESS, message, data);
	}

	public static <T> BaseResponse<T> error() {
		return new BaseResponse<T>(CODE_ERROR, "fail", null);
	}

	public static <T> BaseResponse<T> error(String message) {
		return new BaseResponse<T>(CODE_ERROR, message, null);
	}

	public static <T> BaseResponse<T> error(T data) {
		return new BaseResponse<T>(CODE_ERROR, "fail", data);
	}

	public static <T> BaseResponse<T> error(String message, T data) {
		return new BaseResponse<T>(CODE_ERROR, message, data);
	}

}
