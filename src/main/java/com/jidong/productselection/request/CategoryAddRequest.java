package com.jidong.productselection.request;

import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdProduct;
import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/1 15:00
 * @Description:
 */
@Data
public class CategoryAddRequest {

	private JdProduct product;

	private List<JdCategory> preCategories;

	private String newCategoryName;

	private Boolean isShow;

	private Boolean isMainCate;

	private Boolean isInstallation;

	private Boolean isShelf;

	private Integer categoryOrder;
}
