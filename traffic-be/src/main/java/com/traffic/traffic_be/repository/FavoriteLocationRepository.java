package com.traffic.traffic_be.repository;

import com.traffic.traffic_be.entity.FavoriteLocation;
import com.traffic.traffic_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteLocationRepository extends JpaRepository<FavoriteLocation, Long> {
    List<FavoriteLocation> findByUser(User user);
    boolean existsByLatitudeAndLongitudeAndUser(Double latitude, Double longitude, User user);
}
