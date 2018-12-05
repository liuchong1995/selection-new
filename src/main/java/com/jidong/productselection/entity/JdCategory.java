package com.jidong.productselection.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
* Created by Mybatis Generator 2018/11/22
*/
@Data
@Accessors(chain = true)
public class JdCategory {
    private Integer categoryId;

    private String categoryName;

    private Integer productId;

    private Integer categoryLevel;

    private Integer parentId;

    private Boolean isLeaf;

    private String componentsId;

    private Integer categoryOrder;

    private String categoryProperties;

    private Boolean isShow;

    private List<JdCategory> children = new ArrayList<>();
}