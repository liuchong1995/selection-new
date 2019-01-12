package com.jidong.productselection.request;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2019/1/12 15:52
 * @Description:
 */
@Data
public class AdvanceMandatoryConstraintRequest {

	private Integer productId;

	private List<JdCategory> exitCate;

	private List<JdComponent> exitComp;

	private List<JdCategory> nonExistentCate;

	private List<JdComponent> nonExistentComp;

	private List<JdCategory> resCate;

	private List<JdComponent> resComp;

	private Integer groupId;

	private String groupName;

	private String constraintDesc;
}
