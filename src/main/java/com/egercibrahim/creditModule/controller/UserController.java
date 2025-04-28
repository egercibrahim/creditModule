package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.enums.Role;
import com.egercibrahim.creditModule.model.User;
import com.egercibrahim.creditModule.record.CreateUserRequest;
import com.egercibrahim.creditModule.record.UserRecord;
import com.egercibrahim.creditModule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserRecord> findAll() {
        return userService.findAll()
                .stream().map(User::toRecord).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserRecord createUser(@RequestBody CreateUserRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.valueOf(request.role().toUpperCase().trim()))
                .build();
        return userService.save(user)
                .toRecord();
    }
}
