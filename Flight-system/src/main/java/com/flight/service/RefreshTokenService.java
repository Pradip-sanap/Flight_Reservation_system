package com.flight.service;

import com.flight.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken saveRefreshTokenInDB(String username, String refreshToken);
    RefreshToken verifyRefreshToken(String token);
}
