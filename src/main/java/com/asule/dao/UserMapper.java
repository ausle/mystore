package com.asule.dao;

import com.asule.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //检查用户名是否存在
    int selectUserName(String username);

    int selectEmail(String email);

    User selectLogin(@Param(value ="username") String username, @Param(value ="password")String password);


    //根据用户名查找问题
    String selectQuestionByUsername(@Param(value ="username") String username);


    //检查某个用户的问题答案是否正确
    int checkAnswer(
            @Param(value ="username") String username,
            @Param(value ="question") String question,@Param(value ="answer")String answer);


    //修改密码
    int updatePasswordByUsername(@Param(value ="username") String username, @Param(value ="password")String passwordNew);


    //检查旧密码是否正确
    int checkPassword(@Param(value ="passwordOld") String passwordOld,@Param(value = "id") Integer id);


    //检查邮箱是否存在
    int checkEmailByUserId(@Param(value ="email") String email,@Param(value = "id") Integer id);




}