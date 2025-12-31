package tcbv.zhaohui.moon.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.exceptions.DBException;
import tcbv.zhaohui.moon.exceptions.OssException;
import tcbv.zhaohui.moon.utils.Rsp;

import javax.validation.ConstraintViolationException;

/**
 * @author: zhaohui
 * @Title: ExceptionHandlerAdvice
 * @Description:
 * @date: 2025/12/31 8:54
 */
@Slf4j
@RestControllerAdvice(basePackages = "tcbv.zhaohui.moon")
public class ExceptionHandlerAdvice {
    public static final int VALIDATE_FAILED = 10001;

    @ExceptionHandler(Exception.class)
    public Rsp handleException(Exception e) {
        log.error("", e);
        return Rsp.error(-1, e.getMessage());
    }

    @ExceptionHandler(ChainException.class)
    public Rsp handleException(ChainException e) {
        log.error("", e);
        return Rsp.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(OssException.class)
    public Rsp handleException(OssException e) {
        log.error("", e);
        return Rsp.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DBException.class)
    public Rsp handleException(DBException e) {
        log.error("", e);
        return Rsp.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public Rsp handleException(BizException e) {
        log.error("", e);
        return Rsp.error(e.getCode(), e.getMessage());
    }

    /**
     * {@code @RequestBody} 参数校验不通过时抛出的异常处理
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Rsp handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        if (msg.endsWith(", ")) {
            msg = msg.substring(0, msg.length() - 2);
        }
        if (StringUtils.hasText(msg)) {
            return Rsp.error(VALIDATE_FAILED, msg);
        }
        return Rsp.error(VALIDATE_FAILED, "未知错误");
    }

    /**
     * {@code @PathVariable} 和 {@code @RequestParam} 参数校验不通过时抛出的异常处理
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public Rsp handleConstraintViolationException(ConstraintViolationException ex) {
        if (StringUtils.hasText(ex.getMessage())) {
            return Rsp.error(VALIDATE_FAILED, ex.getMessage());
        }
        return Rsp.error(VALIDATE_FAILED, "");
    }
}
