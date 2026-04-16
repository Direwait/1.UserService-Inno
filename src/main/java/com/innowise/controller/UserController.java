package com.innowise.controller;

import com.innowise.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserController {

    /**
     * Creates a new user profile.
     * Called by API Gateway during registration orchestration.
     * Email must be unique across the system.
     *
     *
     * @param userDto the user profile data containing email, name, surname, birthDate
     * @return ResponseEntity containing created user profile with generated UUID and timestamps
     * @throws com.innowise.exception.DuplicateEmailException if email already exists
     * @throws com.innowise.exception.CardLimitException if user has more than 5 cards
     */
    ResponseEntity<UserDto> createUser(UserDto userDto);

    /**
     * Retrieves a user profile by their id.
     *
     * @param userId the UUID of the user
     * @return ResponseEntity containing user profile with all associated cards
     * @throws jakarta.persistence.EntityNotFoundException if user not found
     */
    ResponseEntity<UserDto> getUserById(UUID userId);

    /**
     * Retrieves a user profile by email address.
     * <p>
     * Used by Order Service for user validation when processing orders.
     * Email is the global identifier across Auth, User and Order services.
     * </p>
     *
     * @param email the user's email address
     * @return ResponseEntity containing user profile
     * @throws jakarta.persistence.EntityNotFoundException if no user with given email exists
     */
    ResponseEntity<UserDto> getUserByEmail(String email);

    /**
     * Updates an existing user profile by Id.
     * <p>
     * Only users with ADMIN role can perform this operation.
     * The updatedAt timestamp is automatically refreshed.
     * </p>
     *
     * @param userId the UUID of the user to update
     * @param userDto the updated user profile data
     * @return ResponseEntity containing updated user profile
     * @throws jakarta.persistence.EntityNotFoundException if user not found
     */
    ResponseEntity<UserDto> updateUserById(UUID userId, UserDto userDto);

    /**
     * Retrieves all users with pagination and optional search.
     * Only users with ADMIN role can perform this operation.
     * Search is performed by name or surname.
     *
     * @param searchTerm optional search term for filtering by name or surname
     * @param pageable pagination parameters (default size = 10)
     * @return ResponseEntity containing page of user profiles
     */
    ResponseEntity<Page<UserDto>> getAllUsers(String searchTerm, Pageable pageable);

    /**
     * Activates or deactivates a user by Id.
     * Only users with ADMIN role can perform this operation.
     * Inactive users cannot authenticate or place orders.
     *
     * @param userId the UUID of the user
     * @return ResponseEntity containing new active status (true = activated, false = deactivated)
     * @throws jakarta.persistence.EntityNotFoundException if user not found
     */
    ResponseEntity<Boolean> activateDeactivateUser(UUID userId);

    /**
     * Deletes a user by ID.
     * Only users with ADMIN role can perform this operation.
     * This is a hard delete that removes the user from the database.
     *
     * @param userId the UUID of the user to delete
     * @return ResponseEntity with 204 No Content status
     * @throws jakarta.persistence.EntityNotFoundException if user not found
     */
    ResponseEntity<Void> deleteById(UUID userId);

    /**
     * Retrieves multiple users by their IDs.
     * Only users with ADMIN role can perform this operation.
     * Useful for batch operations and bulk data retrieval.
     *
     * @param ids list of user UUIDs
     * @return ResponseEntity containing list of user profiles
     */
    ResponseEntity<List<UserDto>> getUsersByIds(List<UUID> ids);
}
