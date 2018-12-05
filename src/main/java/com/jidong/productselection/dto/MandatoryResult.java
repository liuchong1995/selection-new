package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/15 11:30
 * @Description:
 */
@Data
public class MandatoryResult {

	private List<JdCategory> categories;

	private List<JdComponent> components;
}
