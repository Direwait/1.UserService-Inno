package com.innowise.service;

import com.innowise.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(UUID userId);

    UserDto getUserByEmail(String email);

    UserDto updateUserById(UUID userId, UserDto userDto);

    boolean activateDeactivateUser(UUID userId);

    Page<UserDto> getAllUsers(String searchTerm, Pageable pageable);

    void deleteById(UUID userId);
}
