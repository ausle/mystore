package com.asule.service.impl;

import com.asule.common.Constant;
import com.asule.common.ResponseCode;
import com.asule.common.ServerResponse;
import com.asule.dao.CartMapper;
import com.asule.dao.ProductMapper;
import com.asule.entity.Cart;
import com.asule.entity.Product;
import com.asule.service.ICartService;
import com.asule.util.BigDecimalUtil;
import com.asule.util.PropertiesUtil;
import com.asule.vo.CartProductVo;
import com.asule.vo.CartVo;
import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService implements ICartService{

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null){
            //这个产品不在这个购物车里,需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Constant.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        }else{
            //这个产品已经在购物车里了.
            //如果产品已存在,数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {

        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        /*
            如果该商品从未添加到购物车，
            select * from cart where userId=1 and productId=null
            返回的是空。而不是该用户购买记录。
            上面已经判断为null。所以不会出现该情况。

            如果是新的条目，返回null。不会被添加。
         */
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        return this.list(userId);
    }


    /*
        获取购物车VO数据，
        它其中最主要的包括：
            购物车商品的列表(每一个购物车条目对应一个商品，两者封装为一个购物车商品对象)
            总价格：每个购物车商品对象的价格等于商品*数量
    */
    public ServerResponse<CartVo> list (Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    private CartVo getCartVoLimit(Integer userId){

        CartVo cartVo = new CartVo();
        //某个用户的购物车条目列表
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem : cartList){

                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());


                //根据id，获取商品的详细信息
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product!=null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());


                    //判断库存和买的数量，如果买的数量大于库存，则计算为库存。
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Constant.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Constant.Cart.LIMIT_NUM_FAIL);

                        //更新购物车有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }

                    cartProductVo.setQuantity(buyLimitCount);
                    //计算某个商品的总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                //每个购物车条目如果选中的话，就添加到总价格中
                if(cartItem.getChecked() == Constant.Cart.CHECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(true);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }



}

/*

          userid          productId             count
             1                  1               10
             2                  2               5
             3                  3               6





*/
