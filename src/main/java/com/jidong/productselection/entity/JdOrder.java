package com.jidong.productselection.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mybatis Generator 2019/02/18
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

    private Integer status;

    private String message;

    public JdOrder() {
    }

    protected boolean canEqual(Object other) {
        return other instanceof com.jidong.productselection.entity.JdOrder;
    }
}