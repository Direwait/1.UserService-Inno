package com.innowise.controller;

import com.innowise.service.dto.CardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CardController {

    /**
     * Creates a new card.
     * Only users with ADMIN role can perform this operation.
     * Card is associated with a user via userId in CardDto.
     *
     * @param cardDto the card data containing number, holder, expiry, and userId
     * @return ResponseEntity containing created card with generated UUID
     * @throws com.innowise.exception.CardLimitException if user already has 5 cards
     */
    ResponseEntity<CardDto> createCard(CardDto cardDto);

    /**
     * Retrieves a card by its ID.
     *
     * @param cardId the UUID of the card
     * @return ResponseEntity containing card details
     * @throws jakarta.persistence.EntityNotFoundException if card not found
     */
    ResponseEntity<CardDto> getCardById(UUID cardId);

    /**
     * Updates an existing card by ID.
     * Only users with ADMIN role can perform this operation.
     *
     * @param cardId the UUID of the card to update
     * @param cardDto the updated card data
     * @return ResponseEntity containing updated card
     * @throws jakarta.persistence.EntityNotFoundException if card not found
     */
    ResponseEntity<CardDto> updateCardById(UUID cardId, CardDto cardDto);

    /**
     * Retrieves all cards with pagination and optional filter by card number.
     * Only users with ADMIN role can perform this operation.
     *
     * @param number optional card number filter (partial match)
     * @param pageable pagination parameters (default size = 10)
     * @return ResponseEntity containing page of cards
     */
    ResponseEntity<Page<CardDto>> getAllCards(String number, Pageable pageable);

    /**
     * Retrieves all cards belonging to a specific user.
     * Used by API Gateway when fetching user profiles with their cards.
     * Users can view their own cards; ADMIN can view any user's cards.
     *
     * @param userId the UUID of the user
     * @return ResponseEntity containing list of cards associated with the user
     */
    ResponseEntity<List<CardDto>> getAllCardsByUserId(UUID userId);

    /**
     * Activates or deactivates a card by ID.
     * Only users with ADMIN role can perform this operation.
     * Deactivated cards cannot be used for payments.
     *
     * @param cardId the UUID of the card
     * @return ResponseEntity containing new active status (true = activated, false = deactivated)
     * @throws jakarta.persistence.EntityNotFoundException if card not found
     */
    ResponseEntity<Boolean> activateDeactivateCard(UUID cardId);

    /**
     * Deletes a card by ID.
     * Only users with ADMIN role can perform this operation.
     * This is a hard delete that removes the card from the database.
     *
     * @param cardId the UUID of the card to delete
     * @return ResponseEntity with 204 No Content status
     * @throws jakarta.persistence.EntityNotFoundException if card not found
     */
    ResponseEntity<Void> deleteById(UUID cardId);
}
