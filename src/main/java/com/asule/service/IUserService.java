package com.asule.service;

import com.asule.common.ServerResponse;
import com.asule.entity.User;

public interface IUserService {


    /**
     * 注册
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);


    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username,String password);


    /**
     * 查找用户注册时填写的问题
     * @param username
     * @return
     */
    ServerResponse selectQuestion(String username);


    /**
     * 验证某个用户的问题答案是否正确
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username,String question,String answer);


    /**
     * 重置密码(需要传入token，该token必须确定问题答案才可以返还)
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);


    /**
     * 修改密码(无需传入token，但要保证旧密码的正确性)
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);


    /**
     * 更改用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateInformation(User currentUser,User user);



    ServerResponse<User> getInformation(Integer userId);



}
