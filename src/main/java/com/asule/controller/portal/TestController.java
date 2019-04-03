package com.asule.controller.portal;


import com.asule.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@RequestMapping("/test/")
@Controller
public class TestController {


    @ResponseBody
    @RequestMapping("natapp.do")
    public ServerResponse testNatApp(){
        HashMap<String, String> maps = new HashMap<>();
        maps.put("result","success");
        return ServerResponse.createBySuccess(maps);
    }


}
