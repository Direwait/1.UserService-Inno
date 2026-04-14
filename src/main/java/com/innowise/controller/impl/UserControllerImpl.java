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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        var user = userService.createUser(userDto);
        return ResponseEntity.ok(user);
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @GetMapping("/a/debug-headers")
    public ResponseEntity<Map<String, String>> debugHeaders(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String userRole,
            @RequestHeader(value = "X-Username", required = false) String username,
            @RequestHeader(value = "Authorization", required = false) String auth) {

        Map<String, String> headers = new HashMap<>();
        headers.put("X-User-Id", userId != null ? userId : "MISSING");
        headers.put("X-User-Role", userRole != null ? userRole : "MISSING");
        headers.put("X-Username", username != null ? username : "MISSING");
        headers.put("Authorization", auth != null ? "PRESENT (starts with Bearer?)" : "MISSING");

        return ResponseEntity.ok(headers);
    }
}
