package com.innowise.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.innowise.controller.UserController;
import com.innowise.service.UserService;
import com.innowise.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        System.out.println("=== User Controller ===");
        System.out.println("Received user: id=" + userDto.getId() + ", email=" + userDto.getEmail());
        System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());

        var user = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        var userById = userService.getUserById(userId);
        return ResponseEntity.ok(userById);
    }

    @GetMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<List<UserDto>> getUsersByIds(@RequestParam List<UUID> ids) {
        var usersByIds = userService.getUsersByIds(ids);
        return ResponseEntity.ok(usersByIds);
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        var userByEmail = userService.getUserByEmail(email);
        return ResponseEntity.ok(userByEmail);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<UserDto> updateUserById(@PathVariable UUID userId, @Valid @RequestBody UserDto userDto) {
        var updateUserById = userService.updateUserById(userId, userDto);
        return ResponseEntity.ok(updateUserById);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam(required = false) String searchTerm,
                                                     @PageableDefault(size = 10) Pageable pageable) {
        var allCards = userService.getAllUsers(searchTerm, pageable);
        return ResponseEntity.ok(allCards);
    }

    @PatchMapping("/active/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Boolean> activateDeactivateUser(@PathVariable UUID userId) {
        var userActive = userService.activateDeactivateUser(userId);
        return ResponseEntity.ok(userActive);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
