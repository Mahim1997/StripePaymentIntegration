package com.stripeintegration.sampleproject.model;

import lombok.Data;

/***
 * @implNote: Assumes customer already exists
 */

// TODO: Need to add validation logic eg. 3 letters CVC etc
@Data
public class CardDetails {
    private String customerId; // Assumes customer already exists
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvc;
    private String brand = "Visa"; // default "Visa"
}
