package com.Visa.My_First_Spring.repository;

import com.Visa.My_First_Spring.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    
    // Find token by token string
    Optional<VerificationToken> findByToken(String token);
    
    // Find valid token by token string
    @Query("SELECT vt FROM VerificationToken vt WHERE vt.token = :token AND vt.used = false AND vt.expiresAt > :currentTime")
    Optional<VerificationToken> findValidToken(@Param("token") String token, @Param("currentTime") LocalDateTime currentTime);
    
    // Find token by email and type
    @Query("SELECT vt FROM VerificationToken vt WHERE vt.email = :email AND vt.tokenType = :tokenType AND vt.used = false AND vt.expiresAt > :currentTime")
    Optional<VerificationToken> findValidTokenByEmailAndType(@Param("email") String email, 
                                                            @Param("tokenType") VerificationToken.TokenType tokenType,
                                                            @Param("currentTime") LocalDateTime currentTime);
    
    // Mark token as used
    @Modifying
    @Transactional
    @Query("UPDATE VerificationToken vt SET vt.used = true, vt.usedAt = :usedAt WHERE vt.token = :token")
    void markTokenAsUsed(@Param("token") String token, @Param("usedAt") LocalDateTime usedAt);
    
    // Delete expired tokens
    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken vt WHERE vt.expiresAt < :currentTime")
    void deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
    
    // Delete used tokens older than specified time
    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken vt WHERE vt.used = true AND vt.usedAt < :cutoffTime")
    void deleteOldUsedTokens(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    // Delete all tokens for a specific email
    @Modifying
    @Transactional
    void deleteByEmail(String email);
}