package tcbv.zhaohui.moon.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.exceptions.TransactionException;
import tcbv.zhaohui.moon.service.IEthereumService;

/**
 * @author: zhaohui
 * @Title: Web3TxGuardAspect
 * @Description:
 * @date: 2025/12/20 20:41
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class Web3TxGuardAspect {
    @Autowired
    private IEthereumService ethereumService;

    @Around("@annotation(guard)")
    public Object aroundMethod(ProceedingJoinPoint pjp, Web3TxGuard guard) throws Throwable {
        return proceed(pjp, guard);
    }

    @Around("@within(guard) && !@annotation(tcbv.zhaohui.moon.exceptions.Web3TxGuard)")
    public Object aroundClass(ProceedingJoinPoint pjp, Web3TxGuard guard) throws Throwable {
        return proceed(pjp, guard);
    }

    private Object proceed(ProceedingJoinPoint pjp, Web3TxGuard guard) throws Throwable {
        try {
            return pjp.proceed();
        } catch (TransactionException te) {
            String className = pjp.getSignature().getDeclaringTypeName();
            String methodName = pjp.getSignature().getName();
            String reason = ethereumService.parseTransactionException(te);
            log.error("{}:{} => {}", className, methodName, reason);
            // 你也可以换成自定义业务异常，比如 BizException(finalMsg)
            throw new RuntimeException(reason);
        }
    }
}
