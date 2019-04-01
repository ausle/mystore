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




}
