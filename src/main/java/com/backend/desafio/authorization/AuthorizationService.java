package com.backend.desafio.authorization;

import com.backend.desafio.transfer.AuthorizeTransferResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorizationService {
    public boolean authorizeTransfer(Long payerId, Long payeeId, BigDecimal value) {
        try {
            String uri = "https://util.devi.tools/api/v2/authorize";
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("payerId", payerId.toString());
            uriVariables.put("payeeId", payeeId.toString());
            uriVariables.put("value", value.toString());

            AuthorizeTransferResponse result = restTemplate.getForObject(uri, AuthorizeTransferResponse.class, uriVariables);

            assert result != null;
            return result.data().authorization();
        } catch (RestClientException ex) {
            return false;
        }
    }
}
