package com.traffic.traffic_be.repository;

import com.traffic.traffic_be.entity.User;
import com.traffic.traffic_be.entity.UserLicensePlate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLicensePlateRepository extends JpaRepository<UserLicensePlate, Long> {
    List<UserLicensePlate> findByUser(User user);
    boolean existsByLicensePlateAndUser(String licensePlate, User user);
    void deleteByLicensePlateInAndUser(List<String> licensePlates, User user);
}
