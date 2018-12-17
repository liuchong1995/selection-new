package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

/**
 * @Author: LiuChong
 * @Date: 2018/10/25 10:57
 * @Description:
 */
@Data
public class JdComponentExtend {

	private boolean isReverse;

	private JdComponent component;
}
