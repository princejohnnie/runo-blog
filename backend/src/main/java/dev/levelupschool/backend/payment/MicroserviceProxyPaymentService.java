package dev.levelupschool.backend.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MicroserviceProxyPaymentService implements PaymentService {

    @Value("${payments.gatewayUrl}")
    private String gatewayUrl;

    @Value("${payments.gatewayPort}")
    private String gatewayPort;

    @Override
    public ResponseEntity<String> processPayment(PaymentDetailsDto paymentDetailsDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var requestBody = new HashMap<String, Object>();
        requestBody.put("card_holder", paymentDetailsDto.cardHolder);
        requestBody.put("card_number", paymentDetailsDto.cardNumber);
        requestBody.put("card_cvv", paymentDetailsDto.cardCvv);
        requestBody.put("card_expiry_month", paymentDetailsDto.cardExpiryDate.getMonth());
        requestBody.put("card_expiry_year", paymentDetailsDto.cardExpiryDate.getYear());
        requestBody.put("card_pin", paymentDetailsDto.cardPin);
        requestBody.put("email", paymentDetailsDto.email);
        requestBody.put("amount", paymentDetailsDto.amount);
        requestBody.put("phone", paymentDetailsDto.phone);

        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
        String url = gatewayUrl + ":" + gatewayPort + "/execute-payment";

        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(url, request, String.class);
            return response;
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
