package com.asule.controller.backend;

import com.asule.common.Constant;
import com.asule.common.ResponseCode;
import com.asule.common.ServerResponse;
import com.asule.entity.User;
import com.asule.service.impl.CategoryService;
import com.asule.service.impl.UserService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;


    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        //校验一下是否是管理员
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加我们处理分类的逻辑
            return categoryService.addCategory(categoryName,parentId);

        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //更新categoryName
            return categoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }


    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //查询子节点的category信息,不递归子节点的节点
            return categoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }


    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //查询当前节点的id和递归子节点的id
            return categoryService.selectCategoryAndChildrenById(categoryId);

        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }
}
