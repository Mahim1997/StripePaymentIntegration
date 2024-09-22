package com.stripeintegration.sampleproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutSessionResponseDTO {
    private String sessionId;
    private String stripeUrl;
}
