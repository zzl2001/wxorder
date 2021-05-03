package com.imooc.sell.handler;

import com.imooc.sell.exception.SellerAuthorizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Zz1
 * @version 1.0
 * @date 2020/9/29 23:13
 */
@ControllerAdvice
public class SellExceptionHandler {
    //拦截登录异常
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException() {
        return new ModelAndView("redirect:"
                .concat("/sell"));
    }
}
