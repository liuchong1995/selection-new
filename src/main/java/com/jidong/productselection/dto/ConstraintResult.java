package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/25 10:54
 * @Description:
 */
@Data
public class ConstraintResult {

	private List<JdCategory> categories;

	private List<JdComponent> components;

}
