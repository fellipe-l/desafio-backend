package com.backend.desafio.transfer;

import com.backend.desafio.authorization.AuthorizationService;
import com.backend.desafio.exception.ForbiddenTransferException;
import com.backend.desafio.exception.InvalidIdException;
import com.backend.desafio.notification.RabbitMqProducer;
import com.backend.desafio.user.User;
import com.backend.desafio.user.UserService;
import com.backend.desafio.user.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles(value = "test")
class TransferServiceTest {
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private UserService userService;
    @Mock
    private RabbitMqProducer producer;
    @Autowired
    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("transfer should transfer money between two users successfully")
    @Test
    void transfer_should_transfer_money_between_two_users_successfully() throws Exception {
        BigDecimal value = new BigDecimal("10.00");
        User payer = new User(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "12345",
                new BigDecimal("10.00"),
                new UserType(1L, "CUSTOMER")
        );
        User payee = new User(
                2L,
                "Maria",
                "12345678902",
                "maria@email.com",
                "54321",
                new BigDecimal("10.00"),
                new UserType(1L, "CUSTOMER")
        );

        when(userService.findUserById(1L)).thenReturn(payer);
        when(userService.findUserById(2L)).thenReturn(payee);
        when(authorizationService.authorizeTransfer()).thenReturn(true);

        transferService.transfer(value, 1L, 2L);

        verify(userService, times(1)).updateBalance(1L, new BigDecimal("0.00"));
        verify(userService, times(1)).updateBalance(2L, new BigDecimal("20.00"));
        verify(producer, times(1)).produceMessage(anyString());
    }

    @DisplayName("transfer should throw InvalidIdException when IDs are equal")
    @Test
    void transfer_should_throw_InvalidIdException_when_ids_are_equal() {
        Exception thrown = Assertions.assertThrows(InvalidIdException.class, () -> {
            transferService.transfer(new BigDecimal(0), 1L, 1L);
        });

        assertEquals("An user can't transfer to itself.", thrown.getMessage());
    }

    @DisplayName("transfer should throw NullPointerException when payer is null")
    @Test
    void transfer_should_throw_NullPointerException_when_payer_is_null() {
        when(userService.findUserById(1L)).thenReturn(null);
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            transferService.transfer(new BigDecimal(0), 1L, 2L);
        });

        assertEquals("Couldn't find a payer with id " + 1L + ".", thrown.getMessage());
    }

    @DisplayName("transfer should throw NullPointerException when payee is null")
    @Test
    void transfer_should_throw_NullPointerException_when_payee_is_null() {
        User payer = new User(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "12345",
                new BigDecimal("10.00"),
                new UserType(1L, "CUSTOMER")
        );

        when(userService.findUserById(1L)).thenReturn(payer);
        when(userService.findUserById(2L)).thenReturn(null);
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            transferService.transfer(new BigDecimal(0), 1L, 2L);
        });

        assertEquals("Couldn't find a payee with id " + 2L + ".", thrown.getMessage());
    }

    @DisplayName("transfer should throw ForbiddenTransferException when transfer is forbidden")
    @Test
    void transfer_should_throw_ForbiddenTransferException_when_transfer_is_forbidden() {
        User payer = new User(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "12345",
                new BigDecimal("10.00"),
                new UserType(2L, "SHOPKEEPER")
        );
        User payee = new User(
                2L,
                "Maria",
                "12345678902",
                "maria@email.com",
                "54321",
                new BigDecimal("10.00"),
                new UserType(1L, "CUSTOMER")
        );

        when(userService.findUserById(1L)).thenReturn(payer);
        when(userService.findUserById(2L)).thenReturn(payee);
        when(authorizationService.authorizeTransfer()).thenReturn(false);

        Exception thrown = Assertions.assertThrows(ForbiddenTransferException.class, () -> {
            transferService.transfer(new BigDecimal(0), 1L, 2L);
        });

        assertEquals("This transfer hasn't been authorized.", thrown.getMessage());
    }
}
