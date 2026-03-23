package com.innowise.dao.repository;

import com.innowise.dao.model.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardModel, UUID>, JpaSpecificationExecutor<CardModel> {

    int countByUserId(UUID userId);

    @Query("SELECT c FROM CardModel c JOIN FETCH c.user WHERE c.user.id = :userId")
    List<CardModel> findAllCardsByUserIdWithUser(@Param("userId") UUID userId);
}
