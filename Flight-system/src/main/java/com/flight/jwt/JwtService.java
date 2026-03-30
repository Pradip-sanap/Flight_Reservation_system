package com.flight.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private Key getKey(){
        log.debug("Generating signing key from secret");
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    // generate Access Token
    public String generateAccessToken(String username){
        log.info("Generating access token for user: {}", username);
        Map<String, Object> claims = new HashMap<>();
        String newJwtToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        log.debug("Access token generated successfully for user: {}", username);
        return newJwtToken;
    }

    //generate Refresh Token
    public String generateRefreshToken(String username){
        log.info("Generating refresh token for user: {}", username);
        Map<String, Object> claims = new HashMap<>();
        String newRefreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        log.debug("Refresh token generated successfully for user: {}", username);
        return newRefreshToken;
    }

    //extract Username from token
    public String extractUsername(String token){
        log.debug("Extracting username from token");
        String username = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        log.info("Username extracted: {}", username);
        return username;
    }

    // validate
    public boolean validateToken(String token){
        log.info("Validating JWT token");
        String[] parts = token.split("\\.");
        if(parts.length != 3) {
            log.error("Invalid token format: expected 3 parts but got {}", parts.length);
            return false;
        }

        // check is token valid.
        // generate signature using:  header, payload of given token + secret key
        // if generated signature same as the signature which pass in given token, then valid.  Else Invalid.
        try {
            //get claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String userId = claims.getSubject();
            log.info("Token is valid for user: {}", userId);
            return true;
        } catch (ExpiredJwtException e) {
             log.error("token expired");
        } catch (MalformedJwtException e) {
             log.error("invalid format");
        } catch (SecurityException e) {
             log.error("signature mismatch");
        } catch (UnsupportedJwtException e) {
             log.error("unsupported token");
        } catch (IllegalArgumentException e) {
             log.error("empty token");
        }
        return false;
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> expectedClaim) {
        final Claims claims = getAllClaimsFromToken(token);
        return expectedClaim.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

}
