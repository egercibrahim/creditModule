package com.egercibrahim.creditModule.record;

public record CreateUserRequest(String username,
                                String password,
                                String role) {
}