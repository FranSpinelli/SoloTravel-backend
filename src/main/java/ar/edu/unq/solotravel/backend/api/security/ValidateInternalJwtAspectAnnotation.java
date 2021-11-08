package ar.edu.unq.solotravel.backend.api.security;

import ar.edu.unq.solotravel.backend.api.exceptions.InvalidJwtException;
import ar.edu.unq.solotravel.backend.api.helpers.InternalJwtHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(0)
public class ValidateInternalJwtAspectAnnotation {

    @Autowired
    InternalJwtHelper internalJwtHelper;

    //@Value("${tokenCheck.isEnabled}")
    //private Boolean internalTokenCheckIsEnabled;

    @Around("@annotation(ValidateInternalJwt)")
    public Object logExecutionTimeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {

        try{
            String authorizationHeader = Arrays.stream(joinPoint.getArgs()).findFirst().get().toString();

            String travelAgencyEmail = null;
            String jwt = null;

            if (authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                travelAgencyEmail = internalJwtHelper.getEmailFromToken(jwt);
            } else {
                throw new InvalidJwtException();
            }

            if (travelAgencyEmail != null) {
                internalJwtHelper.validateToken(jwt, travelAgencyEmail);
            } else {
                throw new InvalidJwtException();
            }
        }catch(Exception ex){
            throw new InvalidJwtException();
        }
        return joinPoint.proceed();
    }
}
