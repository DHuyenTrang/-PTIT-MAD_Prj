package com.traffic.traffic_be.service.Impl;

import com.traffic.traffic_be.entity.OtpPurpose;
import com.traffic.traffic_be.entity.OtpToken;
import com.traffic.traffic_be.repository.OtpTokenRepository;
import com.traffic.traffic_be.service.EmailService;
import com.traffic.traffic_be.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public String generateAndSendOtp(String email, OtpPurpose purpose) {
        otpTokenRepository.deleteByEmailAndPurpose(email, purpose);

        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpToken token = OtpToken.builder()
                .email(email)
                .otp(otp)
                .purpose(purpose)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        otpTokenRepository.save(token);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi email OTP. Vui lòng thử lại!");
        }

        return otp;
    }

    @Override
    public boolean verifyOtp(String email, String otp, OtpPurpose purpose) {
        return otpTokenRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email, purpose)
                .map(token -> {
                    if (token.getExpiryDate().isBefore(LocalDateTime.now())) return false;
                    return token.getOtp().equals(otp);
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public void markOtpUsed(String email, OtpPurpose purpose) {
        otpTokenRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email, purpose)
                .ifPresent(token -> {
                    token.setUsed(true);
                    otpTokenRepository.save(token);
                });
    }
}
