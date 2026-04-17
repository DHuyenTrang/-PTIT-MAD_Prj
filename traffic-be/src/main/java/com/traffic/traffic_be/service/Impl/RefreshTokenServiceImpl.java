package com.traffic.traffic_be.service.Impl;

import com.traffic.traffic_be.dto.Response.RefreshTokenResponse;
import com.traffic.traffic_be.entity.RefreshToken;
import com.traffic.traffic_be.repository.RefreshTokenRepository;
import com.traffic.traffic_be.service.RefreshTokenService;
import com.traffic.traffic_be.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    @Transactional
    public String createRefreshToken(String email) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .email(email)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshAccessToken(String refreshToken) {
        RefreshToken stored = refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ hoặc đã hết hạn!"));

        if (stored.getExpiryDate().isBefore(LocalDateTime.now())) {
            stored.setRevoked(true);
            refreshTokenRepository.save(stored);
            throw new RuntimeException("Refresh token đã hết hạn. Vui lòng đăng nhập lại!");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        String newAccessToken = jwtUtils.generateToken(stored.getEmail());
        String newRefreshToken = createRefreshToken(stored.getEmail());
        long expireTime = LocalDateTime.now().plusSeconds(jwtExpiration / 1000)
                .toInstant(ZoneOffset.UTC).toEpochMilli();

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expireTime(expireTime)
                .build();
    }

    @Override
    @Transactional
    public void revokeAllTokensByEmail(String email) {
        refreshTokenRepository.revokeAllByEmail(email);
    }
}
