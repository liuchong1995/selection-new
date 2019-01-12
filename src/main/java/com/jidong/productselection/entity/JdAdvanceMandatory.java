package com.jidong.productselection.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
* Created by Mybatis Generator 2019/01/12
*/
@Data
@Accessors(chain = true)
public class JdAdvanceMandatory {
    private Integer mandatoryId;

    private Integer productId;

    private String existCate;

    private String existComp;

    private String nonExistentCate;

    private String nonExistentComp;

    private String mandatory;

    private Boolean isDeleted;
}