package tcbv.zhaohui.moon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tcbv.zhaohui.moon.utils.Rsp;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Rsp<Void> handleBusinessException(BusinessException exception) {
        log.warn("business error: {}", exception.getMessage());
        return Rsp.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Rsp<Void> handleMethodArgumentNotValidException(Exception exception) {
        String message = "参数校验失败";
        if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) exception;
            if (e.getBindingResult().getFieldError() != null) {
                message = e.getBindingResult().getFieldError().getDefaultMessage();
            }
        } else if (exception instanceof BindException) {
            BindException e = (BindException) exception;
            if (e.getBindingResult().getFieldError() != null) {
                message = e.getBindingResult().getFieldError().getDefaultMessage();
            }
        }
        log.warn("validation error: {}", message);
        return Rsp.error(400, message);
    }

    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class})
    public Rsp<Void> handleConstraintViolationException(Exception exception) {
        log.warn("request data error", exception);
        return Rsp.error(400, "请求数据格式错误");
    }

    @ExceptionHandler(Exception.class)
    public Rsp<Void> handleException(Exception exception) {
        log.error("unexpected error", exception);
        return Rsp.error(500, "系统繁忙，请稍后重试");
    }
}
