package com.xie.lifeassistant.controller.core.error;

import io.swagger.annotations.Api;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * Comment：
 * Created by IntelliJ IDEA.
 * User: xie
 * Date: 2020/9/29 15:06
 */
@ApiIgnore()
@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response){
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Object errorMsg = request.getParameter("errorMsg");//MvcExceptionResolver中自定义传递的错误信息
        String url = "/theme/pc/error/000.html?statusCode=" + statusCode;
        if(statusCode == 401 || statusCode == 404 || statusCode == 500){
            url = "/theme/pc/error/" + statusCode + ".html";
        }
        try{
            if(errorMsg != null){
                url += "?errorMsg=" + URLEncoder.encode(errorMsg.toString(), "UTF-8");
            }
            //重定向错误页面
            //response.sendRedirect(url);
            request.getRequestDispatcher(url).forward(request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}