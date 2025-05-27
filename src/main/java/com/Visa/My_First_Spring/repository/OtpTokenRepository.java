package com.Visa.My_First_Spring.repository;


import com.Visa.My_First_Spring.entity.OtpToken;
import com.Visa.My_First_Spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByUser(User user);
}

