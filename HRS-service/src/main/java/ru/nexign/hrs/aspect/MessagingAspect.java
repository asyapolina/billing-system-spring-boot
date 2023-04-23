package ru.nexign.hrs.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.response.Response;

@Aspect
@Component
@Slf4j
public class MessagingAspect {
    private final ObjectMapper mapper;

    @Autowired
    public MessagingAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Pointcut("within(ru.nexign.hrs.messaging..*)" +
            "&& @annotation(org.springframework.jms.annotation.JmsListener)")
    public void pointcut() {}

    @SneakyThrows
    @Before("pointcut()")
    public void beforeRequestReceived(JoinPoint joinPoint) {
        log.info("==> Request received to method {} with args: {}", joinPoint.getSignature().getName(), mapper.writeValueAsString(joinPoint.getArgs().length));
    }

    @AfterReturning(value = "pointcut()", returning = "response")
    public void afterReturningResponse(JoinPoint joinPoint, Response response) throws JsonProcessingException {
        log.info("<== Method {} returned response: {}", joinPoint.getSignature().getName(), mapper.writeValueAsString(response));
    }
}
