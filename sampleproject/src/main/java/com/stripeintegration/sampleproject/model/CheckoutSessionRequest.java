package com.stripeintegration.sampleproject.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class CheckoutSessionRequest {
    @NonNull
    private String customerId; // Assumes customer was already created

    @NonNull
    private String priceId; // Assumes price plans are already created
    private Integer quantity;
    private String currency;
}
