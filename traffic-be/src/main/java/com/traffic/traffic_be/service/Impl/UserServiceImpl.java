package com.traffic.traffic_be.service.Impl;

import com.traffic.traffic_be.dto.Request.ChangePasswordRequest;
import com.traffic.traffic_be.dto.Request.UpdateProfileRequest;
import com.traffic.traffic_be.dto.Response.UserResponse;
import com.traffic.traffic_be.entity.User;
import com.traffic.traffic_be.repository.UserRepository;
import com.traffic.traffic_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Thêm cái này để xử lý mật khẩu

    @Override
    public UserResponse getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return UserResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.isActive())
                .build();
    }

    @Override
    public UserResponse updateMyProfile(UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());

        userRepository.save(user);

        return getMyProfile();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        // 1. Lấy thông tin user hiện tại
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 2. Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác!");
        }

        // 3. Cập nhật mật khẩu mới (đã mã hóa)
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteMyAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Xóa mềm (Soft Delete): Khóa tài khoản lại để giữ nguyên lịch sử tìm kiếm & địa điểm yêu thích
        user.setActive(false);
        userRepository.save(user);

        // Ghi chú: Nếu bạn thực sự muốn xóa bay màu mọi thứ, hãy thay dòng trên bằng:
        // userRepository.delete(user);
    }
}