package com.jidong.productselection.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
* Created by Mybatis Generator 2018/10/28
*/
@Data
@Accessors(chain = true)
public class JdUniMutex {
    private Integer mutexId;

    private Integer productId;

    private String mutexPremise;

    private String mutextResult;

    private Boolean isDeleted;
}