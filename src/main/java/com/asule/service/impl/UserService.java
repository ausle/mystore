package com.asule.service.impl;

import com.asule.common.Constant;
import com.asule.common.ServerResponse;
import com.asule.common.TokenCache;
import com.asule.dao.UserMapper;
import com.asule.entity.User;
import com.asule.service.IUserService;
import com.asule.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService implements IUserService{


    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse serverResponse;

        if (StringUtils.isBlank(user.getUsername())){
            return ServerResponse.createByErrorMessage("用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())){
            return ServerResponse.createByErrorMessage("密码不能为空");
        }
        serverResponse=checkValid(user.getUsername(), Constant.TYPE_USERNAME);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        serverResponse=checkValid(user.getEmail(), Constant.TYPE_EMAIL);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        user.setRole(Constant.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int insertCount = userMapper.insert(user);
        if (insertCount==0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.selectUserName(username);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Constant.TYPE_USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccessMessage(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案是这个用户的,并且是正确的。返回token。
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }


    /**
     * 检查用户名和邮箱是否合法
     * @param str
     * @param type
     * @return
     */
    private ServerResponse checkValid(String str,String type){
        if (StringUtils.isNotBlank(type)){
            if (type== Constant.TYPE_USERNAME){

                int selectUserNameCount = userMapper.selectUserName(str);
                if (selectUserNameCount>0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (type== Constant.TYPE_EMAIL){
/*                if (StringUtils.isBlank(str)){
                    return ServerResponse.createMsgByError("注册时,未填写邮箱");
                }*/
                int selectEmailCount = userMapper.selectEmail(str);
                if (selectEmailCount>0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }
        return ServerResponse.createBySuccess();
    }


    /*
        根据传入的token和本地缓存的token进行比对。(还需要判断该用户存在)
        相等才允许修改密码。
     */
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username,Constant.TYPE_USERNAME);
        if(validResponse.isSuccess()){//需查找到该用户
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }



    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User currentUser,User user) {
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在。查询出来有，就已存在
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
        }

        if (user.getEmail()!=null){
            currentUser.setEmail(user.getEmail());
        }
        if (user.getPhone()!=null){
            currentUser.setPhone(user.getPhone());
        }
        if (user.getQuestion()!=null){
            currentUser.setQuestion(user.getQuestion());
        }
        if (user.getAnswer()!=null){
            currentUser.setAnswer(user.getAnswer());
        }

        int updateCount = userMapper.updateByPrimaryKeySelective(currentUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",currentUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    public ServerResponse checkAdminRole(User user){
        if(user!=null&&user.getRole().intValue()== Constant.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
