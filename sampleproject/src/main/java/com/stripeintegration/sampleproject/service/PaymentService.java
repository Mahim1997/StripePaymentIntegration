package com.stripeintegration.sampleproject.service;

import com.stripe.Stripe;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripeintegration.sampleproject.model.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentService {
    private static final String DEBUG_TAG = "[StripeCheckout] [Service]";

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public void checkout() {
        log.info("{} Stripe.apiKey: {}", DEBUG_TAG, Stripe.apiKey);
    }

    public String createCustomer(CustomerRequest customerRequest) throws Exception {
        Map<String, Object> customerRequestParams = new HashMap<>(){
            {
                put("name", customerRequest.getName());
                put("email", customerRequest.getEmail());
            }
        };
        Customer stripeCustomer = Customer.create(customerRequestParams);
        log.info("{}: creating customer for: {}", DEBUG_TAG, stripeCustomer);
        return stripeCustomer.getId();
    }

    public String createCustomerCard(CardDetails cardDetails) throws Exception {
        Customer customer = Customer.retrieve(cardDetails.getCustomerId());

        Map<String, Object> cardDetailsParams = new HashMap<>() {
            {
                put("number", cardDetails.getCardNumber());
                put("exp_month", cardDetails.getExpiryMonth());
                put("exp_year", cardDetails.getExpiryYear());
                put("cvc", cardDetails.getCvc());
            }
        };
        Map<String, Object> cardTokenParams = new HashMap<>() {
            {
                put("card", cardDetailsParams);
            }
        };
        Token cardToken = Token.create(cardTokenParams);

        Map<String, Object> cardSourceToken = new HashMap<>() {
            {
                put("source", cardToken.getId());
            }
        };

        customer.getSources().create(cardSourceToken);
        return "CREATED_CARD";
    }

    public String chargeCard(PaymentRequest paymentRequest) throws Exception {
        // Now create and confirm the PaymentIntent
        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams
                .builder()
                .setAmount(Long.valueOf(paymentRequest.getAmount()))
                .setCurrency(paymentRequest.getCurrency())
                .setCustomer(paymentRequest.getCustomerId())
                .setConfirm(true)
                .setPaymentMethod("pm_card_visa")
                .setReturnUrl("https://www.wikipedia.org/")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);

        // Handle the result
        if (paymentIntent.getStatus().equals("succeeded")) {
            return "Payment successful!";
        } else if (paymentIntent.getStatus().equals("requires_action")) {
            return "Payment requires further action: " + paymentIntent.getClientSecret();
        } else {
            return "Payment failed: " + paymentIntent.getStatus();
        }
    }

    public CheckoutSessionResponseDTO createCheckoutSession(CheckoutSessionRequest checkoutSessionRequest) throws Exception {
//        success_url: https://www.wikipedia.org/
//        cancel_url: https://www.imdb.com/

        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl("https://www.wikipedia.org")
                .setCancelUrl("https://www.imdb.com/")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(checkoutSessionRequest.getPriceId())
                                .setQuantity(Long.valueOf(checkoutSessionRequest.getQuantity()))
                                .build()
                )
//                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCurrency(checkoutSessionRequest.getCurrency())
                .setCustomer(checkoutSessionRequest.getCustomerId())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .build();

        Session session = Session.create(params);
        return new CheckoutSessionResponseDTO(session.getId(), session.getUrl());
    }
}
