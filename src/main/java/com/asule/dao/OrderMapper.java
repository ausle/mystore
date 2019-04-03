package com.asule.dao;

import com.asule.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);


    Order selectOrderByOrderNoAndUserId(@Param(value = "userId")Integer userId,
                                              @Param(value = "orderNo") Long orderNo
                                              );

    List<Order> selectByUserId(@Param(value = "userId")Integer userId);


    Order selectByOrderNo(@Param(value = "orderNo") Long orderNo);




}