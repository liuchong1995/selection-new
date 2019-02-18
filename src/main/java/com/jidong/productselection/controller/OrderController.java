package com.jidong.productselection.controller;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.dto.OrderDetail;
import com.jidong.productselection.entity.JdOrder;
import com.jidong.productselection.request.GenerateOrderModelNumberRequest;
import com.jidong.productselection.request.OrderSearchRequest;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LiuChong
 * @Date: 2018/12/6 22:42
 * @Description:
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private JdOrderService orderService;

    @PostMapping("/modelNumber")
    public BaseResponse<String> generateModelNumber(@RequestBody GenerateOrderModelNumberRequest modelNumberRequest) {
        try {
            String modelNumber = orderService.generateOrderModelNumber(modelNumberRequest);
            return BaseResponse.success("success", modelNumber);
        } catch (Exception e) {
            log.error("获取模型号错误！" + e.getMessage(), e);
            return BaseResponse.error("获取模型号错误！");
        }
    }

    @PostMapping("/getMandatoryResult")
    public BaseResponse getMandatoryResult(@RequestBody JdOrder order) {
        try {
            return BaseResponse.success(orderService.getMandatoryResult(order));
        } catch (Exception e) {
            log.error("获取必选项错误！" + e.getMessage(), e);
            return BaseResponse.error("获取必选项错误！");
        }
    }

    @PostMapping("/add")
    public BaseResponse insert(@RequestBody JdOrder order) {
        try {
            int n = orderService.insert(order);
            return BaseResponse.success(n);
        } catch (Exception e) {
            log.error("保存订单错误！" + e.getMessage(), e);
            return BaseResponse.error("保存订单错误！");
        }
    }

    @PostMapping("/update")
    public BaseResponse update(@RequestBody JdOrder order) {
        try {
            int n = orderService.update(order);
            return BaseResponse.success(n);
        } catch (Exception e) {
            log.error("更新订单错误！" + e.getMessage(), e);
            return BaseResponse.error("更新订单错误！");
        }
    }

    @PostMapping("/list")
    public BaseResponse<PageInfo<JdOrder>> search(@RequestBody OrderSearchRequest orderSearchRequest) {
        try {
            PageInfo<JdOrder> orderPageInfo = orderService.searchByPage(orderSearchRequest);
            return BaseResponse.success(orderPageInfo);
        } catch (Exception e) {
            log.error("获取订单列表错误！" + e.getMessage(), e);
            return BaseResponse.error("获取订单列表错误！");
        }
    }

    @DeleteMapping("/{orderId}")
    public BaseResponse delete(@PathVariable("orderId") Integer orderId) {
        try {
            int n = orderService.deleteByOrderId(orderId);
            return BaseResponse.success(n);
        } catch (Exception e) {
            log.error("删除订单错误！" + e.getMessage(), e);
            return BaseResponse.error("删除订单错误！");
        }
    }

    @GetMapping("/orderDetail/{orderId}")
    public BaseResponse<OrderDetail> getOrderDetail(@PathVariable("orderId") Integer orderId) {
        try {
            OrderDetail order = orderService.getOrderDetail(orderId, true);
            return BaseResponse.success(order);
        } catch (Exception e) {
            log.error("获取订单错误！" + e.getMessage(), e);
            return BaseResponse.error("获取订单错误！");
        }
    }

    @GetMapping("/{orderId}")
    public BaseResponse<OrderDetail> findOne(@PathVariable("orderId") Integer orderId) {
        try {
            OrderDetail order = orderService.getOrderDetail(orderId, false);
            return BaseResponse.success(order);
        } catch (Exception e) {
            log.error("获取订单错误！" + e.getMessage(), e);
            return BaseResponse.error("获取订单错误！");
        }
    }

    @PostMapping("/commitPreview/{orderId}")
    public BaseResponse commitPreview(@PathVariable("orderId") Integer orderId) {
        try {
            orderService.commitPreview(orderId);
            return BaseResponse.success();
        } catch (Exception e) {
            log.error("提交预览请求错误！" + e.getMessage(), e);
            return BaseResponse.error("提交预览请求错误！");
        }
    }

    @GetMapping("/waitForFinish/{orderId}")
    public BaseResponse waitForFinish(@PathVariable("orderId") Integer orderId) {
        try {
            orderService.waitForFinish(orderId);
            return BaseResponse.success();
        } catch (Exception e) {
            log.error("提交预览请求错误！" + e.getMessage(), e);
            return BaseResponse.error("提交预览请求错误！");
        }
    }
}
