package ar.edu.unq.solotravel.backend.api.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class InternalJwtHelper {
    private static final SignatureAlgorithm sA =SignatureAlgorithm.HS256;
    private static final String secret= "resenia";

    public String getTokenFor(String travelAgencyEmail){

        Map<String, Object> claims = new HashMap<>();
        Instant ins = Instant.now();

        return Jwts.builder().setClaims(claims)
                .setSubject(travelAgencyEmail)
                .setIssuedAt(Date.from(ins))
                .setExpiration(java.util.Date.from(ins.plus(2, ChronoUnit.HOURS)))
                .signWith(sA,secret)
                .compact();
    }

    public Boolean validateToken(String token, String travelAgencyEmail) {
        final String tokenEmail = getEmailFromToken(token);
        return (tokenEmail.equals(travelAgencyEmail) && !isTokenExpired(token));
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        final Date now = new Date();
        return expiration.before(now);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
