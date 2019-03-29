package com.asule.test;

import com.asule.dao.UserMapper;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Test0 extends TestBase {

    Logger logger=Logger.getLogger(Test0.class);

    @Autowired
    private UserMapper userMapper;

    @Test
    public void  test0(){
        logger.info("哈哈哈");
    }

}
