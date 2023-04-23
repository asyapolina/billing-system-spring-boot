package ru.nexign.brt.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.response.Response;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class MessagingAspect {
    private final ObjectMapper mapper;

    @Autowired
    public MessagingAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Pointcut("within(ru.nexign.brt.messaging..*) " +
            "&& @annotation(org.springframework.jms.annotation.JmsListener)")
    public void pointcut() {}

    @SneakyThrows
    @Before("pointcut()")
    public void beforeRequestReceived(JoinPoint joinPoint) {
        Map<String, Object> parameters = getParameters(joinPoint);
        log.info("==> Request received to method {} with args: {}", joinPoint.getSignature().getName(), mapper.writeValueAsString(parameters));
    }

    @AfterReturning(value = "pointcut()", returning = "response")
    public void afterReturningResponse(JoinPoint joinPoint, Response response) throws JsonProcessingException {
        log.info("<== Method {} returned response: {}", joinPoint.getSignature().getName(), mapper.writeValueAsString(response));
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        HashMap<String, Object> map = new HashMap<>();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }
}
