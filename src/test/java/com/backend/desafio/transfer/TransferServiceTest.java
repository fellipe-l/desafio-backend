package com.backend.desafio.transfer;

import com.backend.desafio.authorization.AuthorizationService;
import com.backend.desafio.notification.RabbitMqProducer;
import com.backend.desafio.user.User;
import com.backend.desafio.user.UserService;
import com.backend.desafio.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
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

    @DisplayName("Should transfer money from an user to another successfully")
    @Test
    void shouldTransferMoneyFromAnUserToAnotherSuccessfully() throws Exception {
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
        verify(producer, times(1))
                .produceMessage("Você recebeu uma transferência de " + payer.getFullName() + " no valor de R$" + value);
    }
}