package com.jidong.productselection.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2019/01/12
*/
@Data
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