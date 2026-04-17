package com.traffic.traffic_be.service;

import com.traffic.traffic_be.entity.OtpPurpose;

public interface OtpService {
    String generateAndSendOtp(String email, OtpPurpose purpose);
    boolean verifyOtp(String email, String otp, OtpPurpose purpose);
    void markOtpUsed(String email, OtpPurpose purpose);
}
