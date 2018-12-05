package com.jidong.productselection.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
* Created by Mybatis Generator 2018/10/24
*/
@Data
@Accessors(chain = true)
public class JdMutex {
    private Integer mutexId;

    private Integer productId;

    private Integer mutexType;

    private String mutexPremise;

    private String mutextResult;
}