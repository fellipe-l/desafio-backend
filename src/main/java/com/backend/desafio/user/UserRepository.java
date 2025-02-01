package com.backend.desafio.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "UPDATE users SET balance = :newBalance WHERE id = :id", nativeQuery = true)
    @Transactional
    void updateBalance(@Param("id") Long id, @Param("newBalance") BigDecimal newBalance);
}
