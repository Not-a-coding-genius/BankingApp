package com.Visa.My_First_Spring.repository;

import com.Visa.My_First_Spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Check if user exists by email
    boolean existsByEmail(String email);
    
    // Find active user by email
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.accountActive = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);
    
    // Find verified user by email
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.emailVerified = true")
    Optional<User> findVerifiedUserByEmail(@Param("email") String email);
    
    // Find user by email and password (for login)
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.accountActive = true")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    
    // Update last login time
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :loginTime WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);
    
    // Update email verification status
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.email = :email")
    void verifyUserByEmail(@Param("email") String email);
    
    // Deactivate user account
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountActive = false WHERE u.email = :email")
    void deactivateUserByEmail(@Param("email") String email);
    
    // Update user password
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);
}