package com.asule.controller.portal;

import com.asule.common.Constant;
import com.asule.common.ResponseCode;
import com.asule.common.ServerResponse;
import com.asule.entity.User;
import com.asule.service.IUserService;
import com.asule.service.impl.UserService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService userService;

    @ResponseBody
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    public ServerResponse register(User user){
        return userService.register(user);
    }


    @ResponseBody
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse<User> login = userService.login(username, password);
        if (login.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER,login.getData());
        }
        return login;
    }


    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
    }


    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return userService.checkAnswer(username,question,answer);
    }


    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return userService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return userService.resetPassword(passwordOld,passwordNew,user);
    }


    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser = (User)session.getAttribute(Constant.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        //修改用户信息时，不可以更改username
//        user.setId(currentUser.getId());
        ServerResponse<User> response = userService.updateInformation(currentUser,user);
        if(response.isSuccess()){
//            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Constant.CURRENT_USER,response.getData());
        }
        return response;
    }


    @RequestMapping(value = "get_information.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Constant.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return userService.getInformation(currentUser.getId());
    }

}
