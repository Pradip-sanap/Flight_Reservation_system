package com.flight.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private Key getKey(){
        System.out.println("getkey() method-> " + Keys.hmacShaKeyFor(secret.getBytes()).toString());
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    PasswordEncoder p;

    // generate Access Token
    public String generateAccessToken(String username){
        Map<String, Object> claims = new HashMap<>();
        String newJwtToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("New Generated JWT Token: " + newJwtToken);
        return newJwtToken;
    }

    //generate Refresh Token
    public String generateRefreshToken(String username){
        Map<String, Object> claims = new HashMap<>();
        String newRefreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("New Generated JWT Token: " + newRefreshToken);
        return newRefreshToken;
    }

    //extract Username from token
    public String extractUsername(String token){
        String jwtParsed = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.out.println("Jwt token parse - "+ jwtParsed);
        return jwtParsed;
    }

    // validate
    public boolean validateToken(String token){
        String[] parts = token.split("\\.");
        if(parts.length != 3) {
//            log.error("Unexpected Token length");
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
            System.out.println("User Id->" + userId);

            //check token expiry
            Date expiration = claims.getExpiration();
            if(isTokenExpired(token)){
                return false;
            }
            // System.out.println("Expiration -> "+ expiration);

            return true;
        } catch (ExpiredJwtException e) {
            // log.error("token expired");
        } catch (MalformedJwtException e) {
            // log.error("invalid format");
        } catch (SecurityException e) {
            // log.error("signature mismatch");
        } catch (UnsupportedJwtException e) {
            // log.error("unsupported token");
        } catch (IllegalArgumentException e) {
            // log.error("empty token");
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
