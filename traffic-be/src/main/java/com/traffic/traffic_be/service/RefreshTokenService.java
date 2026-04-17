package com.traffic.traffic_be.service;

import com.traffic.traffic_be.dto.Response.RefreshTokenResponse;

public interface RefreshTokenService {
    String createRefreshToken(String email);
    RefreshTokenResponse refreshAccessToken(String refreshToken);
    void revokeAllTokensByEmail(String email);
}
