package com.jidong.productselection.dto;

import com.jidong.productselection.entity.JdCategory;
import lombok.Data;

/**
 * @Auther: LiuChong
 * @Date: 2018/10/25 10:56
 * @Description:
 */
@Data
public class JdCategoryExtend {

	private boolean isReverse;

	private JdCategory category;
}
