package com.jidong.productselection.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LiuChong
 * @Date: 2019-02-18 14:26
 * @Description:
 */
@Getter
public enum OrderStatusEnum {
    UNCOMMITTED(0, "未提交"),

    COMMITTED(1, "已提交"),

    GENERATE_SUCCESS(2, "生成完成"),

    GENERATING(3, "正在生成"),

    GENERATE_FAILURE(4, "生成失败");

    private static final Map<Integer, OrderStatusEnum> code2enumMap = new HashMap<Integer, OrderStatusEnum>();

    static {
        for(OrderStatusEnum st: OrderStatusEnum.values()) {
            code2enumMap.put(st.getCode(), st);
        }
    }

    /**
     * 按code从映射中找到枚举
     * @param statusCode
     * @return
     */
    public static OrderStatusEnum getByCode(Integer statusCode) {
        return code2enumMap.get(statusCode);
    }

    private Integer code;

    private String message;

    OrderStatusEnum(Integer code,String message) {
        this.code = code;
        this.message = message;
    }


}
