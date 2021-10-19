package ar.edu.unq.solotravel.backend.api.security;

import ar.edu.unq.solotravel.backend.api.exceptions.InvalidJwtException;
import ar.edu.unq.solotravel.backend.api.helpers.GoogleJwtHelper;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@Order(0)
public class ValidateGoogleJwtAspectAnnotation {
    @Autowired
    private GoogleJwtHelper googleJwtHelper;

    @Around("@annotation(ValidateGoogleJwt)")
    public Object logExecutionTimeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Optional<Object> tokenHeader = Arrays.stream(joinPoint.getArgs()).findFirst();
            String googleToken = tokenHeader.get().toString();
            googleJwtHelper.verifyGoogleToken(googleToken);
        }
        catch (Exception ex) {
            throw new InvalidJwtException();
        }

        return joinPoint.proceed();
    }
}
