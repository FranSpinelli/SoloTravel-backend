package ar.edu.unq.solotravel.backend.api.helpers;

import ar.edu.unq.solotravel.backend.api.dtos.GoogleProfileDto;
import com.auth0.jwt.JWT;
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

    public void verifyGoogleToken(String token) {
        DecodedJWT jwt = decodeToken(token);

        verifyAudience(jwt);
        verifyIssuer(jwt);
        verifyExpiration(jwt);
    }

    public DecodedJWT decodeToken(String token) {
        token = token.substring(7); // ignore 'Bearer' prefix
        return JWT.decode(token);
    }

    public GoogleProfileDto getProfileInfo(String token) {
        DecodedJWT jwt = decodeToken(token);

        GoogleProfileDto profileDto = new GoogleProfileDto();
        profileDto.setGoogleId(jwt.getClaim("sub").asString());
        profileDto.setName(jwt.getClaim("name").asString());
        profileDto.setEmail(jwt.getClaim("email").asString());
        profileDto.setPicture(jwt.getClaim("picture").asString());

        return profileDto;
    }

    private void verifyAudience(DecodedJWT jwt) {
        String aud = jwt.getClaim("aud").asString();
        if (!aud.equals(G_CLIENT_ID))
            throw new JWTVerificationException("You don't have permission to make this request");
    }

    private void verifyIssuer(DecodedJWT jwt) {
        String issuer = jwt.getClaim("iss").asString();
        if (!issuer.equals(G_ISSUER))
            throw new JWTVerificationException("You don't have permission to make this request");
    }

    private void verifyExpiration(DecodedJWT jwt) {
        Date expiration = jwt.getClaim("exp").asDate();
        if (expiration.before(new Date()))
            throw new JWTVerificationException("Session expired");
    }
}
