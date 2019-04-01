package com.asule.service;

import com.asule.common.ServerResponse;
import com.asule.entity.Product;
import com.asule.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;


public interface IProductService {


    /**
     * 添加或修改商品
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 设置产品状态(1-在售 2-下架 3-删除)
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);


    /**
     * 获取商品详情(PO--->VO的转换)
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);


    /**
     * 分页查询商品列表(使用pageHelper)
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);


    /**
     * 分页查询列表(添加筛选条件)
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,Integer pageNum, Integer pageSize);


    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);


    /**
     * 模糊搜索商品(分页+排序)
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,
                                                         Integer categoryId,
                                                         int pageNum,
                                                         int pageSize,
                                                         String orderBy);


}
