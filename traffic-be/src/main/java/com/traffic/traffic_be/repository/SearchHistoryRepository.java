package com.traffic.traffic_be.repository;

import com.traffic.traffic_be.entity.SearchHistory;
import com.traffic.traffic_be.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    void deleteByUser(User user);
}
