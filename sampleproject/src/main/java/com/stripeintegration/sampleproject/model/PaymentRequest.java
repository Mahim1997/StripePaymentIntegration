package com.stripeintegration.sampleproject.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
    private String customerId;
    private String currency;
    private Integer amount;
    private CardDetails cardDetails;
}
