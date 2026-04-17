package com.traffic.traffic_be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String otp; // Lưu mã OTP 6 số

    @Column(nullable = false)
    private LocalDateTime expiryDate; // Thời gian hết hạn (VD: 15 phút)

    @Builder.Default
    private boolean isUsed = false; // Đánh dấu xem OTP này đã xài chưa

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}