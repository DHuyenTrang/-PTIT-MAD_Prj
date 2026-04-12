package com.traffic.traffic_be.service.Impl;

import com.traffic.traffic_be.dto.Request.LoginRequest;
import com.traffic.traffic_be.dto.Request.RegisterRequest;
import com.traffic.traffic_be.dto.Response.LoginResponse;
import com.traffic.traffic_be.entity.User;
import com.traffic.traffic_be.repository.PasswordResetTokenRepository;
import com.traffic.traffic_be.repository.UserRepository;
import com.traffic.traffic_be.service.AuthService;
import com.traffic.traffic_be.service.EmailService;
import com.traffic.traffic_be.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.traffic.traffic_be.entity.PasswordResetToken;
import com.traffic.traffic_be.repository.PasswordResetTokenRepository;
import com.traffic.traffic_be.service.EmailService;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Override
    public String register(RegisterRequest request) {
        // 1. Chuyển sang check trùng lặp bằng Email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        User user = User.builder()
                // Bỏ dòng .username() đi
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Mã hóa BCrypt
                .fullName(request.getFullName())
                // .phone(request.getPhone()) // Có thể mở comment dòng này nếu DTO của bạn có truyền số điện thoại
                .build();

        userRepository.save(user);
        return "Đăng ký thành công!";
    }
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Sai email hoặc mật khẩu!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai email hoặc mật khẩu!");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return LoginResponse.builder()
                .accessToken(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    @Transactional
    public String forgotPassword(String email) {
        // 1. Kiểm tra email có tồn tại không
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này!"));

        // 2. Xóa các OTP cũ của user này (nếu có) để tránh rác DB
        tokenRepository.deleteByUser(user);

        // 3. Sinh mã OTP 6 số ngẫu nhiên
        String otp = String.format("%06d", new Random().nextInt(999999));

        // 4. Lưu OTP vào Database với thời hạn 15 phút
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .otp(otp)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(resetToken);

        // 5. Gửi thư (Nên đưa vào try-catch để nếu lỗi mail thì báo cho Frontend)
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi email. Vui lòng thử lại sau!");
        }

        return "Mã OTP đã được gửi đến email của bạn!";
    }

    @Override
    @Transactional
    public String resetPassword(String email, String otp, String newPassword) {
        // 1. Tìm user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        // 2. Tìm OTP trong DB
        PasswordResetToken resetToken = tokenRepository.findByOtpAndUserAndIsUsedFalse(otp, user)
                .orElseThrow(() -> new RuntimeException("Mã OTP không hợp lệ hoặc đã được sử dụng!"));

        // 3. Kiểm tra xem OTP có hết hạn chưa (So sánh expiryDate với thời gian hiện tại)
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã OTP đã hết hạn!");
        }

        // 4. Mã hóa mật khẩu mới và lưu vào User
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 5. Đánh dấu OTP này đã được sử dụng
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return "Đặt lại mật khẩu thành công!";
    }


}