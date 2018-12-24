package com.jidong.productselection.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: LiuChong
 * @Date: 2018/12/24 13:03
 * @Description:
 */
public class DateUtil {

	public static String getDateString(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}
}
