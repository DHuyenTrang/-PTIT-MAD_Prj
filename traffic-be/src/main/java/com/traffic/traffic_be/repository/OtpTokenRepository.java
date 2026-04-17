package com.traffic.traffic_be.repository;

import com.traffic.traffic_be.entity.OtpPurpose;
import com.traffic.traffic_be.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(String email, OtpPurpose purpose);

    @Modifying
    @Transactional
    @Query("DELETE FROM OtpToken o WHERE o.email = :email AND o.purpose = :purpose")
    void deleteByEmailAndPurpose(String email, OtpPurpose purpose);
}
