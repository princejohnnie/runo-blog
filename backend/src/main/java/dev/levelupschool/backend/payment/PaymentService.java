package dev.levelupschool.backend.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PaymentService {
    ResponseEntity<String> processPayment(PaymentDetailsDto paymentDetailsDto);
}
