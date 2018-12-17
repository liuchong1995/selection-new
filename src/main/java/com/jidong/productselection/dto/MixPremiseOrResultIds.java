package com.jidong.productselection.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/24 17:05
 * @Description:
 */
@Data
public class MixPremiseOrResultIds {

	private List<Integer> categoryIds;

	private List<Integer> componentIds;
}
