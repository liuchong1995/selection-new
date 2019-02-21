package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdOrder;
import com.jidong.productselection.request.OrderSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Mybatis Generator 2019/02/18
 */
@Mapper
public interface JdOrderMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(JdOrder record);

    int insertSelective(JdOrder record);

    JdOrder selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(JdOrder record);

    int updateByPrimaryKey(JdOrder record);

    List<JdOrder> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

    List<JdOrder> findByOrderSearchRequest(@Param("searchRequest") OrderSearchRequest searchRequest);

    int deleteByOrderId(@Param("orderId") Integer orderId);

    int deleteByOrderIdIn(@Param("orderIdList")List<Integer> orderIdList);

    Integer findNextOrderId();

    List<JdOrder> findOneDayOrder(@Param("createTime") Date createTime);

    int updateStatusByOrderId(@Param("updatedStatus")Integer updatedStatus,@Param("orderId")Integer orderId);

    int updateMessageByOrderId(@Param("updatedMessage")String updatedMessage,@Param("orderId")Integer orderId);


}