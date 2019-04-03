package com.asule.service;

import com.asule.common.ServerResponse;
import com.asule.vo.CartVo;

public interface ICartService {

    /**
     * 添加商品到购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);


    /**
     * 更新商品数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);


    /**
     * 获取某个用户的购物车VO
     * @param userId
     * @return
     */
    ServerResponse<CartVo> list(Integer userId);


    /**
     * 删除某个用户购物车的商品(可以删除多个)
     * @param userId
     * @param productIds
     * @return
     */
    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);


    /**
     * 改变某个用户购物车商品的状态值(全选或全不选)
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked);


    /**
     * 获取某个用户购物车商品的总数量
     * @param userId
     * @return
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);



}
