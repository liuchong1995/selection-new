package com.jidong.productselection.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
* Created by Mybatis Generator 2018/12/15
*/
@Data
public class JdOrder implements Serializable {
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

    private String mainCompCode;
}