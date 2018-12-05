package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/10/24 17:15
 * @Description:
 */
@Data
public class MixPremiseOrResult {

	private List<JdCategory> categories;

	private List<JdComponent> components;
}
