package com.jidong.productselection.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/9 09:10
 * @Description:
 */
@Data
@Accessors(chain = true)
public class ComponentAddRequest {

	private Integer productId;

	private String componentName;

	private String componentReamrk;

	private String componentModelNumber;

	private String componentShortNumber;

	private String componentDetail;

	private List<Integer> categoryIds;

	private String componentImage;

	private Integer componentType;

	private String componentKey;

	private String creator;
}
