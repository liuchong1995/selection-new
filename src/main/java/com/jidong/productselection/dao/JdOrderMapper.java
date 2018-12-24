package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdOrder;
import com.jidong.productselection.request.OrderSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Mybatis Generator 2018/12/15
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

	Integer findNextOrderId();

	List<JdOrder> findOneDayOrder(@Param("createTime")Date createTime);
}