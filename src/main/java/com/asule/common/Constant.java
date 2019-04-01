package com.asule.common;

import com.google.common.collect.Sets;

import java.util.HashSet;

//常量类
public class Constant {


    public static String CURRENT_USER="CURRENT_USER";

    public static String TYPE_USERNAME="TYPE_USERNAME";
    public static String TYPE_EMAIL="TYPE_EMAIL";


    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public interface Cart{
        int CHECKED = 1;//即购物车选中状态
        int UN_CHECKED = 0;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface ProductListOrderBy{
        HashSet<String> PRICE_ASC_DESC= Sets.newHashSet("price_desc","price_asc");
    }


    public enum ProductStatusEnum{

        ON_SELL(1,"在售"),
        NO_SELL(2,"下架"),
        NONE(3,"删除");

        private int status;
        private String msg;

        ProductStatusEnum(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


}
