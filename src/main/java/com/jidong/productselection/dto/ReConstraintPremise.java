package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
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
public class ReConstraintPremise {

	private List<Integer> categoryIds;

	private List<Integer> componentIds;
}
