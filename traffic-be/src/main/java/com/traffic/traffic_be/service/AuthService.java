package com.traffic.traffic_be.service;

import com.traffic.traffic_be.dto.Request.*;
import com.traffic.traffic_be.dto.Response.*;

public interface AuthService {
    void sendLoginOtp(String email);
    AuthDataResponse loginWithPassword(LoginWithPasswordRequest request);
    AuthDataResponse verifyOtp(VerifyOtpRequest request);
    void sendOrResendOtp(String email);
    void forgotPassword(String email);
    void updatePassword(UpdatePasswordRequest request, String currentUserEmail);
    AccountStatusResponse getAccountStatus(String email);
    UserResponse getInformation();
    void logout(String email);
}
