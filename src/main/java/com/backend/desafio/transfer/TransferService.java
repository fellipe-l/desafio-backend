package com.backend.desafio.transfer;

import com.backend.desafio.authorization.AuthorizationService;
import com.backend.desafio.exception.ForbiddenTransferException;
import com.backend.desafio.exception.InsufficientBalanceException;
import com.backend.desafio.exception.InvalidIdException;
import com.backend.desafio.exception.InvalidPayerTypeException;
import com.backend.desafio.notification.RabbitMqProducer;
import com.backend.desafio.user.User;
import com.backend.desafio.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final RabbitMqProducer producer;

    @Autowired
    public TransferService(AuthorizationService authorizationService, UserService userService, RabbitMqProducer producer) {
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.producer = producer;
    }

    private String generateNotificationMessage(BigDecimal value, String payerName) {
        return "Você recebeu uma transferência de " +
                payerName +
                " no valor de R$" +
                value;
    }

    public void transfer(BigDecimal value, Long payerId, Long payeeId) throws ForbiddenTransferException, InsufficientBalanceException, InvalidIdException, InvalidPayerTypeException {
        if (payerId.equals(payeeId)) throw new InvalidIdException("An user can't transfer to itself.");

        User payer = userService.findUserById(payerId);
        if (payer == null) throw new NullPointerException("Couldn't find a payer with id " + payerId + ".");
        userService.validatePayer(payer, value);

        User payee = userService.findUserById(payeeId);
        if (payee == null) throw new NullPointerException("Couldn't find a payee with id " + payeeId + ".");

        if (authorizationService.authorizeTransfer()) {
            userService.updateBalance(payerId, payer.getBalance().subtract(value));
            userService.updateBalance(payeeId, payee.getBalance().add(value));

            producer.produceMessage(generateNotificationMessage(value, payer.getFullName()));
        } else throw new ForbiddenTransferException("This transfer hasn't been authorized.");
    }
}
