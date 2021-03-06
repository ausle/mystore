package com.asule.common;

public enum ResponseCode{


    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(2,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(3,"ILLEGAL_ARGUMENT");


    private int code;
    private String desc;

    ResponseCode(int code,String desc) {
        this.code=code;
        this.desc=desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
