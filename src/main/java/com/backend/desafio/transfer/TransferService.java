package com.backend.desafio.transfer;

import com.backend.desafio.exception.ForbiddenTransferException;
import com.backend.desafio.exception.InsufficientBalanceException;
import com.backend.desafio.exception.InvalidIdException;
import com.backend.desafio.exception.InvalidPayerTypeException;
import com.backend.desafio.notification.NotificationService;
import com.backend.desafio.user.User;
import com.backend.desafio.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Autowired
    public TransferService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    private boolean authorizeTransfer() {
        try {
            String uri = "https://util.devi.tools/api/v2/authorize";
            RestTemplate restTemplate = new RestTemplate();
            AuthorizeTransfer result = restTemplate.getForObject(uri, AuthorizeTransfer.class);

            assert result != null;
            return result.getData().getAuthorization();
        } catch (RestClientException ex) {
            return false;
        }
    }

    private String generateNotificationMessage(BigDecimal value, String payerName) {
        return "Você recebeu uma transferência de " +
                payerName +
                " no valor de R$" +
                value;

    }

    public void transfer(BigDecimal value, Long payerId, Long payeeId) throws ForbiddenTransferException, InsufficientBalanceException, InvalidIdException, InvalidPayerTypeException {
        if (payerId.equals(payeeId)) throw new InvalidIdException("You can't transfer to yourself.");

        User payer = userRepository.findById(payerId).orElse(null);
        if (payer == null) throw new NullPointerException("Couldn't find a payer with id " + payerId + ".");

        if (payer.getUserType().getType().equalsIgnoreCase("SHOPKEEPER")) throw new InvalidPayerTypeException("A shopkeeper can't transfer money.");
        if (payer.getBalance().compareTo(value) < 0) throw new InsufficientBalanceException("Your balance isn't enough for this transfer.");

        User payee = userRepository.findById(payeeId).orElse(null);
        if (payee == null) throw new NullPointerException("Couldn't find a payee with id " + payeeId + ".");

        if (authorizeTransfer()) {
            userRepository.transfer(
                    payerId,
                    payeeId,
                    payer.getBalance().subtract(value),
                    payee.getBalance().add(value)
            );
            notificationService.notifyPayee(generateNotificationMessage(value, payer.getFullName()));
        } else throw new ForbiddenTransferException("This transfer hasn't been authorized.");
    }
}
