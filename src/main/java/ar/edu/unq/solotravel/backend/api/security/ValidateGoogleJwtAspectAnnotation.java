package ar.edu.unq.solotravel.backend.api.security;

import ar.edu.unq.solotravel.backend.api.helpers.GoogleJwtHelper;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
@Component
@Order(0)
public class ValidateGoogleJwtAspectAnnotation {
    @Autowired
    private GoogleJwtHelper googleJwtHelper;

    @Around("@annotation(ValidateGoogleJwt)")
    public Object logExecutionTimeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        String googleToken = Arrays.stream(joinPoint.getArgs()).findFirst().get().toString();
        googleJwtHelper.verifyGoogleToken(googleToken);

        return joinPoint.proceed();
    }
}
