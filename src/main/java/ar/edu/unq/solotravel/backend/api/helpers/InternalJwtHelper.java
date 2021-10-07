package ar.edu.unq.solotravel.backend.api.helpers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class InternalJwtHelper {
    private static final SignatureAlgorithm sA =SignatureAlgorithm.HS256;
    private static final String secret= "resenia";

    public String getTokenFor(String subject){
        Instant ins = Instant.now();
        return Jwts.builder().setSubject(subject)
                .setIssuedAt(Date.from(ins))
                .setExpiration(java.util.Date.from(ins.plus(2, ChronoUnit.HOURS)))
                .signWith(sA,secret)
                .compact();
    }
}
