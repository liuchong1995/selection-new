package com.jidong.productselection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/12/29 22:09
 * @Description:
 */
@Data
@AllArgsConstructor
public class ReConstraintResult {

	private List<Integer> categoryIds;

	private List<Integer> componentIds;
}
