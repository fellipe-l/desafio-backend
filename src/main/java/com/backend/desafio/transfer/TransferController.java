package com.backend.desafio.transfer;

import com.backend.desafio.exception.ForbiddenTransferException;
import com.backend.desafio.exception.InsufficientBalanceException;
import com.backend.desafio.exception.InvalidIdException;
import com.backend.desafio.exception.InvalidPayerTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("transfer")
    public ResponseEntity<String> transfer(@RequestBody Transfer transfer) {
        try {
            transferService.transfer(transfer.getValue(), transfer.getPayerId(), transfer.getPayeeId());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (ForbiddenTransferException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (InsufficientBalanceException | InvalidPayerTypeException | InvalidIdException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
