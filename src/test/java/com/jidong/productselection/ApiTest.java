package com.jidong.productselection;

import org.junit.Test;

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
}
