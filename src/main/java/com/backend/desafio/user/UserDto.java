package com.backend.desafio.user;

import java.math.BigDecimal;

public record UserDto(String fullName, String document, String email, String password, BigDecimal balance, UserType userType) {
}
