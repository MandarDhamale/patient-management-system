package com.pm.authservice.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component // registers the class as a Java Bean and a Spring Bean - for dependency injection
public class JWTUtil {

    // secret key to generate the token and also to prove the token has come from our servers, basically to verify it
    private final Key secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret){
        byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8)); // converting the string to bytes
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role){
        return Jwts.builder()
                .subject(email) // standard property must include
                .claim("role", role) // custom property
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey) // secret key that we will be having
                .compact(); // take all the prop. create a string and sign with secret key (basically encrypt) and compact and will a create a string with
                // all the information
    }

    public void validateToken(String token){
        try {
            Jwts.parser().verifyWith((SecretKey) secretKey) // verify the signature with the secret key
                    .build().parseSignedClaims(token); // verify with the secret key if the token is valid and given by our server
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e){
            throw new JwtException("Invalid JWT ");
        }
    }





}
