package com.example.helloservice.ratelimit;


import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Aspect
@Component
public class RateLimitAspect {
    private final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object rateLimiting(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = joinPoint.getSignature().toLongString();
        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(key, k -> RateLimiter.create(rateLimit.value()));

        if (rateLimiter.tryAcquire()) {
            return joinPoint.proceed();
        } else {
            throw new TooManyRequestsException("Too many requests. Try again later.");
        }
    }
}

