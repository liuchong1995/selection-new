package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/25 10:52
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstraintPremise {

	private List<JdCategory> categories;

	private List<JdComponent> components;
}
