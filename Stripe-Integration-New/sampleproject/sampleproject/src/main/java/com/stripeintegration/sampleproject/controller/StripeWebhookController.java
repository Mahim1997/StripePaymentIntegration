package com.stripeintegration.sampleproject.controller;

import com.stripeintegration.sampleproject.service.StripeWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final StripeWebhookService stripeWebhookService;

    @PostMapping("/events")
    public ResponseEntity<String> handleEvent(
            @RequestHeader("Stripe-Signature") String stripeSignatureHeader,
            @RequestBody String eventStr
    ) {
        try {
            stripeWebhookService.handleEvent(stripeSignatureHeader, eventStr);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error("Error while handling event: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
