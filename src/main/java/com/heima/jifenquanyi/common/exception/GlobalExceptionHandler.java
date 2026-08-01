package com.heima.jifenquanyi.common.exception;

import com.heima.jifenquanyi.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public R<Void> handleBizException(BizException e) {
        log.warn("业务异常: {}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> handleDuplicateKey(DuplicateKeyException e) {
        log.warn("唯一索引冲突: {}", e.getMessage());
        return R.fail("请勿重复提交");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError() == null
                ? "参数错误" : e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail("系统繁忙，请稍后再试");
    }
}
