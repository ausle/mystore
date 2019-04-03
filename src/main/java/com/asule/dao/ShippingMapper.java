package com.asule.dao;

import com.asule.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import javax.xml.validation.Validator;
import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);


    int deleteByShippingIdUserId(@Param(value = "shippingId") Integer shippingId,
                                 @Param(value = "userId")Integer userId);

    Shipping selectByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId") Integer shippingId);


    List<Shipping> selectByUserId(@Param("userId")Integer userId);

}