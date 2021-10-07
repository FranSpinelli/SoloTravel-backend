package ar.edu.unq.solotravel.backend.api.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GoogleJwtHelper {
    @Value("${oauth.google.issuer}")
    private String G_ISSUER;
    @Value("${oauth.google.client_id}")
    private String G_CLIENT_ID;

    public DecodedJWT verifyGoogleToken(String token) {
        try {
            token = token.substring(7);
            DecodedJWT jwt = decodeToken(token);

            // Verify aud
            boolean isValidAud = verifyAudience(jwt);
            // Verify issuer
            boolean isValidIssuer = verifyIssuer(jwt);
            // Verify expiration
            boolean isNotExpired = verifyExpiration(jwt);

            if (isValidAud && isValidIssuer && isNotExpired)
                return jwt;

        } catch (JWTVerificationException exception){
            //Invalid signature/claims
        }
        return null;
    }

    public DecodedJWT decodeToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt;
        } catch (JWTDecodeException exception){
            //Invalid token
            return null;
        }
    }

    private boolean verifyAudience(DecodedJWT jwt) {
        return jwt.getClaim("aud").asString().equals(G_CLIENT_ID);
    }

    private boolean verifyIssuer(DecodedJWT jwt) {
        return jwt.getClaim("iss").asString().equals(G_ISSUER);
    }

    private boolean verifyExpiration(DecodedJWT jwt) {
        return jwt.getClaim("exp").asDate().after(new Date());
    }
}
