package com.flight.service.impl;

import com.flight.exception.InvalidRefreshTokenException;
import com.flight.exception.RefreshTokenExpiredException;
import com.flight.model.RefreshToken;
import com.flight.repository.RefreshTokenRepository;
import com.flight.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshExp;

    @Override
    public RefreshToken saveRefreshTokenInDB(String username, String refreshToken) {
        log.info("Request received for saving refresh token to DB");
        RefreshToken obj = new RefreshToken(username, refreshToken);
        obj.setExpiryDate(new Date(System.currentTimeMillis() + refreshExp));
        log.info("Refresh token saved successfully");
        return refreshTokenRepository.save(obj);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->
                                                                                new InvalidRefreshTokenException("Refresh Token not valid"));
        if(refreshToken.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException("Refresh Token expired");
        }
        return refreshToken;
    }
}
