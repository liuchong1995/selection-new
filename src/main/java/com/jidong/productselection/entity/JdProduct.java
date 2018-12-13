package com.jidong.productselection.entity;

import lombok.Data;

@Data
public class JdProduct {
    private Integer productId;

    private String productName;

    private String productImg;

    private Integer installationId;

    private Integer shelfId;

    private String segmentation;

    private Integer mainCateid;
}