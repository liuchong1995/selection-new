package com.jidong.productselection.entity;

import lombok.Data;

import java.util.Date;

/**
* Created by Mybatis Generator 2018/11/02
*/
@Data
public class JdComponent {
    private Integer componentId;

    private String componentName;

    private String componentReamrk;

    private String componentModelNumber;

    private String componentShortNumber;

    private String componentDetail;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String componentKey;

    private Integer firstCategoryId;

    private Integer lastCategoryId;

    private String componentImage;

    private Integer productId;

    private Integer componentType;
}