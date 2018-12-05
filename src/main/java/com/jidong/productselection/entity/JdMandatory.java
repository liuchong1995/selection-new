package com.jidong.productselection.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2018/11/15
*/
@Data
public class JdMandatory {
    private Integer mandatoryId;

    private Integer premiseProductId;

    private Integer premiseCategoryId;

    private Integer premiseComponentId;

    private Integer resultCategoryId;

    private Integer resultComponentId;

    private Boolean isDeleted;
}