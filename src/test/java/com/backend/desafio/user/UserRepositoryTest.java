package com.backend.desafio.user;

import com.backend.desafio.EmbeddedPostgresConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {EmbeddedPostgresConfig.class})
@DataJpaTest
@ExtendWith(EmbeddedPostgresConfig.EmbeddedPostgresExtension.class)
class UserRepositoryTest {
    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTypeRepository userTypeRepository;

    @BeforeEach
    void createUserTypes() {
        userTypeRepository.save(new UserType(null, "CUSTOMER"));
        userTypeRepository.save(new UserType(null, "SHOPKEEPER"));
    }

    @DisplayName("Should transfer money from an user to another successfully")
    @Test
    void shouldTransferMoneyFromAnUserToAnotherSuccessfully() {
        BigDecimal value = new BigDecimal("100");
        User user1 = createUser(new UserDto("João", "12345678901", "joao@email.com", "12345", new BigDecimal("5000.00"), new UserType(1L, "CUSTOMER")));
        User user2 = createUser(new UserDto("Maria", "12345678902", "maria@email.com", "54321", new BigDecimal("10000.00"), new UserType(1L, "CUSTOMER")));

        userRepository.updateBalance(user1.getId(), user1.getBalance().subtract(value));
        userRepository.updateBalance(user2.getId(), user2.getBalance().add(value));
        entityManager.clear();

        BigDecimal user1NewBalance = userRepository.findById(user1.getId()).orElse(null).getBalance();
        BigDecimal user2NewBalance = userRepository.findById(user2.getId()).orElse(null).getBalance();
        assertEquals(user1.getBalance().subtract(value), user1NewBalance);
        assertEquals(user2.getBalance().add(value), user2NewBalance);
    }

    private User createUser(UserDto userDto) {
        User newUser = new User(userDto);
        userRepository.save(newUser);

        return newUser;
    }
}