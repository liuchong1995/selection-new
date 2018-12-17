package com.jidong.productselection.request;

import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/10/19 19:44
 * @Description:
 */
@Data
public class RefactorTreeRequest {
	private Integer productId;

	private List<JdComponent> selectedList;
}
