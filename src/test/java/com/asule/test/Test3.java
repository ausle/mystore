package com.asule.test;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class Test3 extends TestCase {


    public void test0() {


        System.out.println(0.05 + 0.01);
        System.out.println(1.0 - 0.42);


        System.out.println(4.0121 * 100);

        System.out.println(123.3 / 12);


    }


    public void test1() {

        BigDecimal bigDecimal1 = new BigDecimal(0.05);
        BigDecimal bigDecimal2 = new BigDecimal(0.01);

        System.out.println(bigDecimal1.add(bigDecimal2));
    }

    //商业中使用BigDecimal，要使用其string类型的构造方法
    public void test2() {
        BigDecimal bigDecimal1 = new BigDecimal("0.05");
        BigDecimal bigDecimal2 = new BigDecimal("0.01");
        System.out.println(bigDecimal1.add(bigDecimal2));
    }
}