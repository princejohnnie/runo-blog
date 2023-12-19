package dev.levelupschool.backend.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    ResponseEntity<?> processPayment(PaymentDetailsDto paymentDetailsDto);
}
