package com.maple.checklist.global.config.log;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Aspect
@Log4j2
@Component
public class LogAspect {
    @Around("execution(* com.maple.checklist.domain..*Controller.*(..)) ||"
        + " execution(* com.maple.checklist.domain..*Service.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        String name = joinPoint.getSignature().getDeclaringTypeName();
        String type =
            StringUtils.contains(name, "Controller") ? "Controller ===> " :
                StringUtils.contains(name, "Service") ? "Service ===> " :
                    "";

        log.info(type + name + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }
}
