package com.punici.gulimall.product.exception;

import com.punici.gulimall.common.exception.BizCodeEnume;
import com.punici.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
// @ResponseBody
// @ControllerAdvice(basePackages = "com.punici.gulimall.product.controller")
@RestControllerAdvice(basePackages = "com.punici.gulimall.product.controller")
public class GulimallExceptionControllerAdvice
{
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e)
    {
        Map<String, String> errorMap = new HashMap<>();
        log.error("数据校验出现问题：{},异常类型：{}", e.getMessage(), e.getClass());
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach(v -> errorMap.put(v.getField(), v.getDefaultMessage()));
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), "数据校验出现问题").put("data", errorMap);
    }
    
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable e)
    {
        
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
