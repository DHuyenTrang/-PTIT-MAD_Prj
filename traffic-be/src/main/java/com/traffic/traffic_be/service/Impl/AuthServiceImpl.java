package com.traffic.traffic_be.service.Impl;

import com.traffic.traffic_be.dto.Request.*;
import com.traffic.traffic_be.dto.Response.*;
import com.traffic.traffic_be.entity.*;
import com.traffic.traffic_be.repository.*;
import com.traffic.traffic_be.service.*;
import com.traffic.traffic_be.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void sendLoginOtp(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));
        otpService.generateAndSendOtp(email, OtpPurpose.LOGIN);
    }

    @Override
    public AuthDataResponse loginWithPassword(LoginWithPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Sai email hoặc mật khẩu!"));

        if (!user.isActive()) {
            throw new RuntimeException("Tài khoản đã bị khóa!");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai email hoặc mật khẩu!");
        }

        return buildAuthData(user);
    }

    @Override
    @Transactional
    public AuthDataResponse verifyOtp(VerifyOtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtpCode();

        boolean isExistingUser = userRepository.existsByEmail(email);

        if (isExistingUser) {
            OtpPurpose purpose = determinePurposeForExistingUser(email, otp);

            if (!otpService.verifyOtp(email, otp, purpose)) {
                throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn!");
            }
            otpService.markOtpUsed(email, purpose);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

            if (!user.isActive()) {
                throw new RuntimeException("Tài khoản đã bị khóa!");
            }
            return buildAuthData(user);
        } else {
            if (!otpService.verifyOtp(email, otp, OtpPurpose.REGISTER)) {
                throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn!");
            }
            otpService.markOtpUsed(email, OtpPurpose.REGISTER);

            User newUser = User.builder()
                    .email(email)
                    .fullName(request.getDisplayName() != null ? request.getDisplayName() : email.split("@")[0])
                    .password(request.getPassword() != null
                            ? passwordEncoder.encode(request.getPassword())
                            : null)
                    .build();
            userRepository.save(newUser);
            return buildAuthData(newUser);
        }
    }

    private OtpPurpose determinePurposeForExistingUser(String email, String otp) {
        for (OtpPurpose p : OtpPurpose.values()) {
            if (otpService.verifyOtp(email, otp, p)) return p;
        }
        return OtpPurpose.LOGIN;
    }

    @Override
    public void sendOrResendOtp(String email) {
        boolean exists = userRepository.existsByEmail(email);
        OtpPurpose purpose = exists ? OtpPurpose.LOGIN : OtpPurpose.REGISTER;
        otpService.generateAndSendOtp(email, purpose);
    }

    @Override
    public void forgotPassword(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));
        otpService.generateAndSendOtp(email, OtpPurpose.FORGOT_PASSWORD);
    }

    @Override
    @Transactional
    public void updatePassword(UpdatePasswordRequest request, String currentUserEmail) {
        if (request.getOtpCode() != null && request.getEmail() != null) {
            String email = request.getEmail();
            if (!otpService.verifyOtp(email, request.getOtpCode(), OtpPurpose.FORGOT_PASSWORD)) {
                throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn!");
            }
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            otpService.markOtpUsed(email, OtpPurpose.FORGOT_PASSWORD);
        } else if (currentUserEmail != null) {
            User user = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
            if (request.getOldPassword() == null
                    || !passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("Mật khẩu cũ không chính xác!");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Thiếu thông tin để đổi mật khẩu!");
        }
    }

    @Override
    public AccountStatusResponse getAccountStatus(String email) {
        return userRepository.findByEmail(email)
                .map(user -> AccountStatusResponse.builder()
                        .isRegistered(true)
                        .hasPassword(user.getPassword() != null)
                        .build())
                .orElse(AccountStatusResponse.builder()
                        .isRegistered(false)
                        .hasPassword(false)
                        .build());
    }

    @Override
    public UserResponse getInformation() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        return UserResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.isActive())
                .build();
    }

    @Override
    @Transactional
    public void logout(String email) {
        refreshTokenService.revokeAllTokensByEmail(email);
    }

    private AuthDataResponse buildAuthData(User user) {
        String accessToken = jwtUtils.generateToken(user.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        long expireTime = System.currentTimeMillis() + 86400000L;

        return AuthDataResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expireTime(expireTime)
                .customerId(user.getId() != null ? user.getId().intValue() : null)
                .displayName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}
