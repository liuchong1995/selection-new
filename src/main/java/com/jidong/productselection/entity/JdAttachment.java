package com.jidong.productselection.entity;

import lombok.Data;

/**
* Created by Mybatis Generator 2018/11/02
*/
@Data
public class JdAttachment {
    private Integer attachId;

    private Integer productId;

    private Integer masterId;

    private Integer attachmentId;

    private Boolean isDeleted;
}