package com.example.JWTlogin.JWT;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Component
@Slf4j
public class JWTutil {
    private static final String ISSUER = "UJ07 Sever";

    private JWTutil(){}
    private static SecretKey  secretKey= Jwts.SIG.HS256.key().build();
    public static boolean validatetoken(String jwttoken) {
        return parseToken(jwttoken).isPresent();
    }

    private static Optional<Claims> parseToken(String jwttoken) {
        var jwtParser= Jwts.parser().verifyWith(secretKey).build();
        try{
            return Optional.of(jwtParser.parseEncryptedClaims(jwttoken).getPayload());
        }catch (JwtException e){
            log.error("JWT exception occur");
        }catch (IllegalArgumentException e){
            throw  new RuntimeException(e);
        }
        return  Optional.empty();
    }

    public static Optional<String> getusernameformtoken(String jwttoken) {
        var claims=parseToken(jwttoken);

        return claims.map(Claims::getSubject);

    }

    public static String generateToken(String email) {
        var currentDate=new Date();
        var tokenexp=500000;
        var expiration=new Date(currentDate.getTime()+ tokenexp);
        return Jwts.builder().id(UUID.randomUUID().toString()).issuer(ISSUER).subject(email).signWith(secretKey)
                .issuedAt(currentDate).expiration(expiration)
                .compact();
    }
}
