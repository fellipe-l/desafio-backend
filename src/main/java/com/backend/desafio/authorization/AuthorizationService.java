package com.backend.desafio.authorization;

import com.backend.desafio.transfer.AuthorizeTransfer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorizationService {
    public boolean authorizeTransfer() {
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
}
