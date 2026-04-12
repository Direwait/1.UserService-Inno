package com.innowise.dao.repository;


import com.innowise.dao.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.cards WHERE u.id = :userId")
    Optional<UserModel> findUserWithCardsById(@Param("userId") UUID userId);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.cards WHERE u.email = :userEmail")
    Optional<UserModel> findUserWithCardsByEmail(@Param("userEmail") String userEmail);

    boolean existsByEmail(String email);
}
