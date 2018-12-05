package com.jidong.productselection.request;

import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/10/21 10:27
 * @Description:
 */
@Data
public class OptionalListBySelectedRequest {
	private Integer categoryId;

	private List<JdComponent> selectedList;
}
