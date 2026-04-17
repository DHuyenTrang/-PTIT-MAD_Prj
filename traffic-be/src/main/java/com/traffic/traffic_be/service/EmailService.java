package com.traffic.traffic_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Mã OTP Khôi phục mật khẩu - Traffic App");
        message.setText("Xin chào,\n\n"
                + "Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản Traffic App.\n"
                + "Mã OTP của bạn là: " + otp + "\n\n"
                + "Mã này sẽ hết hạn sau 15 phút. Vui lòng không chia sẻ mã này cho bất kỳ ai để bảo mật tài khoản.\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ Traffic App");
        mailSender.send(message);
    }
}