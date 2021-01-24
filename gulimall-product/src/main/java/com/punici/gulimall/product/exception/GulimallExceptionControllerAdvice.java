package com.punici.gulimall.product.exception;

import com.punici.gulimall.common.exception.BizCodeEnume;
import com.punici.gulimall.common.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

// @ResponseBody
// @ControllerAdvice(basePackages = "com.punici.gulimall.product.controller")
@RestControllerAdvice(basePackages = "com.punici.gulimall.product.controller")
public class GulimallExceptionControllerAdvice
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GulimallExceptionControllerAdvice.class);
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e)
    {
        Map<String, String> errorMap = new HashMap<>();
        LOGGER.error("数据校验出现问题：{},异常类型：{}", e.getMessage(), e.getClass());
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach(v -> errorMap.put(v.getField(), v.getDefaultMessage()));
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), "数据校验出现问题").put("data", errorMap);
    }
    
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable e)
    {
        LOGGER.error("系统未知错误:", e);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
