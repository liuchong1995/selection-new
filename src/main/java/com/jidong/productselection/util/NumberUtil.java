package com.jidong.productselection.util;

/**
 * @Author: LiuChong
 * @Date: 2018/12/24 11:47
 * @Description:
 */
public class NumberUtil {

	public static String formatThousand(int num){
		return num > 99 ? String.valueOf(num) : String.format("%03d", num);
	}

	public static String formatTen(int num){
		return num > 9 ? String.valueOf(num) : String.format("%02d", num);
	}

	public static int thousandToNum(String num){
		return Integer.parseInt(num);
	}
}
