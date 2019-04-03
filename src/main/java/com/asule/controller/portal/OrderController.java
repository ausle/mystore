package com.asule.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.asule.common.Constant;
import com.asule.common.ResponseCode;
import com.asule.common.ServerResponse;
import com.asule.entity.User;
import com.asule.service.impl.OrderService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import net.sf.jsqlparser.schema.Server;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@RequestMapping("/order/")
@Controller
public class OrderController {

    Logger logger=Logger.getLogger(OrderService.class);


    @Autowired
    private OrderService orderService;


    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return orderService.pay(orderNo,user.getId(),path);
    }



    @RequestMapping("alipay_callbak.do")
    @ResponseBody
    public ServerResponse alipayCallback(HttpServletRequest request){

        //支付宝回调时，各种信息都在request中。从request中取出相关信息
        Map<String, String> params =new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Iterator<String> iterator =
                parameterMap.keySet().iterator();

        while (iterator.hasNext()){
            String key=iterator.next();
            String[] values = parameterMap.get(key);
            String value="";
            for (int i = 0; i < values.length; i++) {
                value+=values[i]+",";
            }
            value=value.substring(0,value.length()-1);
            params.put(key,value);
        }

        logger.info("签名："+params.get("sign"));
        logger.info("交易状态："+params.get("trade_status"));
        logger.info("所有参数："+params.toString());


        params.remove("sign_type");

        //验证回调的正确(是不是支付宝发的)(避免重复通知)
        //对返回结果的验签
        try {
            /**
             *  rsaCheckV2仅仅只除去了sign，然后才对其他参数进行字典排序，组成字符串，得到待签名字符串。
             *  所以在调用该方法前，需要除去sign_type。
             *
             *  我们使用的签名类型是rsa2，所以使用SHA256WithRSA的signture来处理处理验签。
             */
            boolean rsaCheckV2 = AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(), "utf-8", Configs.getSignType());

            if (!rsaCheckV2){
                return ServerResponse.createByErrorMessage("非法请求,验证不通过");
            }
        } catch (AlipayApiException e) {
            logger.info("支付宝验证回调异常");
        }

        //验证各种数据



        //

        return null;


    }


    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.createOrder(user.getId(),shippingId);
    }


    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.cancel(user.getId(),orderNo);
    }

    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderCartProduct(user.getId());
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderList(user.getId(),pageNum,pageSize);
    }





}
