package com.jidong.productselection.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
* Created by Mybatis Generator 2018/10/28
*/
@Data
@Accessors(chain = true)
public class JdMutexDescribe {
    private Integer describeId;

    private Integer productId;

    private Integer constraintType;

    private String mutexIds;

    private String constraintDesc;

    private Integer groupId;

    private String groupName;

    private Boolean isDeleted;
}