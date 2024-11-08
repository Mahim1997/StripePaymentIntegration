package com.stripeintegration.sampleproject.controller;

import com.stripeintegration.sampleproject.model.*;
import com.stripeintegration.sampleproject.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/customer")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerRequest customerRequest) {
        try {
            return ResponseEntity.ok(paymentService.createCustomer(customerRequest));
        } catch (Exception e) {
            log.error("Error while creating customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Creating customer error: " + e.getMessage());
        }
    }

    @PostMapping("/customer-card")
    public ResponseEntity<String> createCardForCustomer(@RequestBody CardDetails cardDetails) {
        try {
            return ResponseEntity.ok(paymentService.createCustomerCard(cardDetails));
        } catch (Exception e) {
            log.error("Error while creating customer card: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Creating customer card error: " + e.getMessage());
        }
    }

    @PostMapping("/charge")
    public ResponseEntity<String> chargeCard(@RequestBody PaymentRequest chargeRequest) {
        try {
            return ResponseEntity.ok(paymentService.chargeCard(chargeRequest));
        } catch (Exception e) {
            log.error("Error while charging card: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while charging card: " + e.getMessage());
        }
    }

    @PostMapping("/checkout-session")
    public ResponseEntity<CheckoutSessionResponseDTO> createCheckoutSession(@RequestBody CheckoutSessionRequest checkoutSessionRequest) {
        try {
            return ResponseEntity.ok(paymentService.createCheckoutSession(checkoutSessionRequest));
        } catch (Exception e) {
            log.error("Error while checkout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
