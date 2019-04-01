package com.asule.dao;

import com.asule.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);


    List<Product> selectList();


    List<Product> selectByNameAndProductId(@Param(value = "productName")String productName,
                                           @Param(value = "productId")Integer productId);


    List<Product> selectByNameAndCategoryIds(@Param(value = "keyword")String keyword,
                                             @Param(value = "ids")List<Integer> ids);


}