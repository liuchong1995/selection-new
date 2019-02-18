package com.jidong.productselection;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/12/24 11:52
 * @Description:
 */
public class ApiTest {

	@Test
	public void test1(){
		String num = "025";
		int i = Integer.parseInt(num);
		System.out.println();
	}

	@Test
	public void test2(){
		HashMap<Integer, List<Integer>> hashMap = new HashMap<>();
		hashMap.put(1, new ArrayList<>(Collections.singleton(1)));
		List<Integer> integers = hashMap.get(1);
		integers.add(2);
		System.out.println();
	}

	@Test
	public void test3(){
		String a = "hello - world";
		System.out.println(JSON.toJSONString(a));
	}


	private String PREVIEW_LOCATION = "/Users/liuchong/AutoDresserPRJ/";

	private static String PREVIEW_SUFFIX = ".EASM";
	@Test
	public void test4(){
		File file = new File("/Users/liuchong/AutoDresserPRJ/GZJD01-20190124-001/CDKRRB-F10FS10KC322-CVSP1JB15.EASM");
		String path = PREVIEW_LOCATION + "GZJD01-20190124-001" + File.separator + "CDKRRB-F10FS10KC322-CVSP1JB15.EASM";
		File file1 = new File(path);

		System.out.println(file1.exists());
	}
}
