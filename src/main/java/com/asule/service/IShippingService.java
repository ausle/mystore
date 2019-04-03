package com.asule.service;

import com.asule.common.ServerResponse;
import com.asule.entity.Shipping;
import com.github.pagehelper.PageInfo;

public interface IShippingService {


    /**
     * 添加地址，返回地址id
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse add(Integer userId, Shipping shipping);


    /**
     * 根据id删除地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<String> del(Integer userId,Integer shippingId);


    /**
     * 修改地址信息
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse update(Integer userId, Shipping shipping);


    /**
     * 查询某个地址的信息
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);


    /**
     * 查询所有地址信息(分页)
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);


}
