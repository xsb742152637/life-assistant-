package com.xie.lifeassistant.util.exception;

import com.xie.lifeassistant.util.context.AppendParameterRequest;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Comment：
 * Created by IntelliJ IDEA.
 * User: xie
 * Date: 2020/9/29 15:01
 */
@Component
public class MvcExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        //首先在控制台打印出错误信息
        ex.printStackTrace();

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String errorMsg="";
            boolean isAjax= "1".equals(request.getParameter("isAjax"));

            //ex 为业务层抛出的自定义异常
            if(ex instanceof ServiceCustomException){
                ServiceCustomException customEx=    (ServiceCustomException)ex;

                errorMsg ="customStatus:"+customEx.getCustomStatus() +",customMessage:"+customEx.getCustomMessage()
                        +"\r\n"+ ExceptionUtils.getStackTrace(customEx);
            }else{
                request.setAttribute("javax.servlet.error.status_code", 500);
                //ex为非自定义异常，则
                errorMsg=ExceptionUtils.getStackTrace(ex);
            }

            AppendParameterRequest myRequest = new AppendParameterRequest(request);
            myRequest.setParameter("errorMsg", errorMsg);

            if(isAjax){

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(errorMsg);
                return new   ModelAndView();
            }else{
                //否则，  输出错误信息到自定义的500.jsp页面
                request.getRequestDispatcher("/error").forward(myRequest,response);
                //ModelAndView mv = new ModelAndView("/error");
                //mv.addObject("aaa", errorMsg);
                //return mv ;
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        return new ModelAndView();

    }
}
