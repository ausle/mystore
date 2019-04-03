package com.asule.dao;

import com.asule.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);


    Cart selectCartByUserIdProductId(@Param(value = "userId") Integer userId, @Param(value = "productId")Integer productId);

    List<Cart> selectCartByUserId(@Param(value = "userId") Integer userId);

    int deleteByUserIdProductIds(@Param(value = "userId") Integer userId,@Param(value = "productIds")List<String> productIds);


    int checkedOrUncheckedProduct(@Param(value = "userId") Integer userId,
                                  @Param(value = "productId")Integer productId,
                                  @Param(value = "checked")Integer checked);


    int selectCartProductCount(@Param(value = "userId") Integer userId);


    int isJudgeAllChecked(@Param(value = "userId") Integer userId);


    List<Cart> selectCheckedCartByUserId(@Param(value = "userId") Integer userId);








}