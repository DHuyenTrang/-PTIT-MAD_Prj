package com.traffic.traffic_be.service.Impl;

import com.traffic.traffic_be.entity.User;
import com.traffic.traffic_be.entity.UserLicensePlate;
import com.traffic.traffic_be.repository.UserLicensePlateRepository;
import com.traffic.traffic_be.repository.UserRepository;
import com.traffic.traffic_be.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final UserRepository userRepository;
    private final UserLicensePlateRepository licensePlateRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
    }

    @Override
    public void addLicensePlate(String licensePlate) {
        User user = getCurrentUser();
        if (licensePlateRepository.existsByLicensePlateAndUser(licensePlate, user)) {
            throw new RuntimeException("Biển số này đã được thêm trước đó!");
        }
        licensePlateRepository.save(UserLicensePlate.builder()
                .licensePlate(licensePlate)
                .user(user)
                .build());
    }

    @Override
    public List<String> getLicensePlates() {
        User user = getCurrentUser();
        return licensePlateRepository.findByUser(user).stream()
                .map(UserLicensePlate::getLicensePlate)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLicensePlates(List<String> licensePlates) {
        User user = getCurrentUser();
        licensePlateRepository.deleteByLicensePlateInAndUser(licensePlates, user);
    }
}
