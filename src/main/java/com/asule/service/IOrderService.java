package com.asule.service;


import com.asule.common.ServerResponse;
import com.asule.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface IOrderService {





    /**
     * 创建订单
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse createOrder(Integer userId,Integer shippingId);


    /**
     * 取消订单：把订单状态改变为已取消
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse cancel(Integer userId,Long orderNo);


    /**
     * 返回OrderProductVo数据给前端
     * @param userId
     * @return
     */
    ServerResponse getOrderCartProduct(Integer userId);


    /**
     * 返回OrderVo订单详情给前端
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);


    /**
     * 获取订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


    /**
     * 调用支付宝支付
     * @param orderNo
     * @param userId
     * @param path      存储二维码图片的路径
     * @return
     */
    ServerResponse pay(Long orderNo, Integer userId, String path);


    /**
     * 处理支付宝的回调
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String,String> params);

}
