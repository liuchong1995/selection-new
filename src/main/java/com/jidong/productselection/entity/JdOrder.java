package com.jidong.productselection.entity;

import lombok.Data;

import java.util.Date;

/**
* Created by Mybatis Generator 2018/11/22
*/
@Data
public class JdOrder {
    private Integer orderId;

    private String orderNumber;

    private Integer productId;

    private String productModel;

    private String customer;

    private String endUser;

    private String creator;

    private String modifier;

    private Date createTime;

    private Date updateTime;

    private Boolean isDeleted;

    private String relateSellId;

    private String componentIds;

    private Integer shelfHeight;

    private Integer mountHeight;

    private String shelfCode;
}