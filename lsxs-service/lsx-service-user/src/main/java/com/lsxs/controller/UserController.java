package com.lsxs.controller;


import com.lsxs.entity.AuthToken;
import com.lsxs.entity.Result;
import com.lsxs.entity.StatusCode;
import com.lsxs.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    //客户端id
    @Value("${auth.clientId}")
    private String clientId;

    //客户端密钥
    @Value("${auth.clientSecret}")
    private String clientSecret;


    @Autowired
    private UserLoginService userLoginService;


    @RequestMapping("/login")
    public Result login(String username, String password){



        //授权模式
        String grantType = "password";
        AuthToken authToken = null;

        try {
            authToken = userLoginService.login(username, password, clientId, clientSecret, grantType);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new Result(false, StatusCode.LOGINERROR, "登陆失败 : "+e.getMessage());
        }

        if (authToken != null){
            return new Result(true, StatusCode.OK, "登陆成功", authToken);
        }


        return new Result(false, StatusCode.LOGINERROR, "登陆失败");
    }



}
