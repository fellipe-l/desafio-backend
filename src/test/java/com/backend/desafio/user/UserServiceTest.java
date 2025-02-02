package com.backend.desafio.user;

import com.backend.desafio.exception.InsufficientBalanceException;
import com.backend.desafio.exception.InvalidPayerTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("findUserById should return an user successfully")
    @Test
    void findUserById_should_return_an_user_successfully() {
        Optional<User> user = Optional.of(
                new User(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "12345",
                new BigDecimal("10.00"),
                new UserType(1L, "CUSTOMER")
        ));

        when(userRepository.findById(1L)).thenReturn(user);
        User result = userService.findUserById(1L);
        assertEquals(user.get(), result);
    }

    @DisplayName("findUserById should return null when looking for an user that does not exist")
    @Test
    void findUserById_should_return_null_when_looking_for_an_user_that_does_not_exist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        User result = userService.findUserById(1L);
        assertEquals(null, result);
    }

    @DisplayName("updateBalance should update an user balance successfully")
    @Test
    void updateBalance_should_update_an_user_balance_successfully() {
        assertDoesNotThrow(() -> userService.updateBalance(1L, new BigDecimal(20)));
    }

    @DisplayName("updateBalance should throw RuntimeException when occurs a SQL error")
    @Test
    void updateBalance_should_throw_RuntimeException_when_occurs_a_sql_error() {
        doThrow(RuntimeException.class).when(userRepository).updateBalance(1L, new BigDecimal(20));

        assertThrows(RuntimeException.class, () -> {
            userService.updateBalance(1L, new BigDecimal(20));
        });
    }

    @DisplayName("validatePayer should validate a payer successfully")
    @Test
    void validatePayer_should_validate_a_payer_successfully() {
        User payer = new User(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "12345",
                new BigDecimal("10.00"),
                new UserType(1L, "CUSTOMER")
        );

        assertDoesNotThrow(() -> userService.validatePayer(payer, new BigDecimal(5)));
    }

    @DisplayName("validatePayer should throw InvalidPayerTypeException when payer is a shopkeeper")
    @Test
    void validatePayer_should_throw_InvalidPayerTypeException_when_payer_is_a_shopkeeper() {
        User payer = new User(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "12345",
                new BigDecimal("10.00"),
                new UserType(1L, "SHOPKEEPER")
        );

        Exception thrown = assertThrows(InvalidPayerTypeException.class, () -> {
            userService.validatePayer(payer, new BigDecimal(5));
        });

        assertEquals("A shopkeeper can't transfer money.", thrown.getMessage());
    }

    @DisplayName("validatePayer should throw InsufficientBalanceException when payer balance is not enough to transfer")
    @Test
    void validatePayer_should_throw_InsufficientBalanceException_when_payer_balance_is_not_enough_to_transfer() {
        User payer = new User(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "12345",
                new BigDecimal("1.00"),
                new UserType(1L, "CUSTOMER")
        );

        Exception thrown = assertThrows(InsufficientBalanceException.class, () -> {
            userService.validatePayer(payer, new BigDecimal(5));
        });

        assertEquals("Your balance isn't enough for this transfer.", thrown.getMessage());
    }
}