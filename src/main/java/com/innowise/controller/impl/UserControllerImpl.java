package com.innowise.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.innowise.controller.UserController;
import com.innowise.service.UserService;
import com.innowise.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @PostMapping()
    @Override
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        var user = userService.createUser(userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    @Override
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        var userById = userService.getUserById(userId);
        return ResponseEntity.ok(userById);
    }

    @PutMapping("/{userId}")
    @Override
    public ResponseEntity<UserDto> updateUserById(@PathVariable UUID userId, @Valid @RequestBody UserDto userDto) {
        var updateUserById = userService.updateUserById(userId, userDto);
        return ResponseEntity.ok(updateUserById);
    }

    @GetMapping()
    @Override
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam(required = false) String searchTerm,
                                                     @PageableDefault(size = 10) Pageable pageable) {
        var allCards = userService.getAllUsers(searchTerm, pageable);
        return ResponseEntity.ok(allCards);
    }

    @PatchMapping("/active/{userId}")
    @Override
    public ResponseEntity<Boolean> activateDeactivateUser(@PathVariable UUID userId) {
        var userActive = userService.activateDeactivateUser(userId);
        return ResponseEntity.ok(userActive);
    }

    @DeleteMapping("/{userId}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
