package com.jidong.productselection.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2018/11/26
*/
@Data
public class JdProduct {
    private Integer productId;

    private String productName;

    private String productImg;

    private Integer installationId;

    private Integer shelfId;

    private String segmentation;
}