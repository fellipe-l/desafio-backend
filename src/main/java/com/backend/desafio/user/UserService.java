package com.backend.desafio.user;

import com.backend.desafio.exception.InsufficientBalanceException;
import com.backend.desafio.exception.InvalidPayerTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void updateBalance(Long id, BigDecimal newBalance) {
        userRepository.updateBalance(id, newBalance);
    }

    public void validatePayer(User payer, BigDecimal value) throws InsufficientBalanceException, InvalidPayerTypeException{
        if (payer.getUserType().getType().equalsIgnoreCase("SHOPKEEPER")) throw new InvalidPayerTypeException("A shopkeeper can't transfer money.");
        if (payer.getBalance().compareTo(value) < 0) throw new InsufficientBalanceException("Your balance isn't enough for this transfer.");
    }
}
