package com.example.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(
                    ExceptionLoggingAspect.class);

    @AfterThrowing(
            pointcut =
                    "execution(* com.example.library.service.BorrowingService.borrowBook(..))"
                            + " || execution(* com.example.library.service.BorrowingService.returnBook(..))",
            throwing = "exception"
    )
    public void logException(
            JoinPoint joinPoint,
            Throwable exception) {

        log.error(
                "Operation failed: {}. Error: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage()
        );
    }
}