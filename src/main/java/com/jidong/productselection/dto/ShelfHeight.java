package com.jidong.productselection.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/12/3 13:59
 * @Description:
 */
@Data
public class ShelfHeight {

	private Integer height;

	private Integer minMountedHeight;

	private List<Integer> inst;
}
