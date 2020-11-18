package com.love.strutly.exception.handler;

import com.love.strutly.exception.BusinessException;
import com.love.strutly.exception.code.BaseExceptionType;
import com.love.strutly.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {


    @ExceptionHandler(value = BusinessException.class)
    <T> DataResult<T> businessExceptionHandler(BusinessException e) {
        return new DataResult<>(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    <T> DataResult<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }

    private <T> DataResult<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        int i = 0;
        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            log.info("msg={}",msgs[i]);
            i++;
        }
        return DataResult.getResult(BaseExceptionType.USER_ERROR.getCode(), msgs[0]);
    }

    @ExceptionHandler(Exception.class)
    <T> DataResult<T> exception(Exception e) {
        log.error(e.toString());
        return new DataResult<>(BaseExceptionType.OTHER_ERROR);
    }
}
