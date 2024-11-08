package com.stripeintegration.sampleproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import com.stripeintegration.sampleproject.model.WebhookEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripeWebhookService {
    private static String DEBUG_TAG = "[Stripe][Webhook][Service]";

    @Value("${STRIPE_WEBHOOK_ENDPOINT_SECRET}")
    private String stripeWebhookEndpointSecret;

    public String handleEvent(String stripeSignatureHeader, String eventStr) throws Exception {
        Event event = Webhook.constructEvent(eventStr, stripeSignatureHeader, stripeWebhookEndpointSecret);
        // log.info("{} Obtained event: {}", DEBUG_TAG, event);

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        log.info("Event API version: {}", event.getApiVersion());
        if (dataObjectDeserializer.getObject().isEmpty()) {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            log.error("Error while deserializing event to stripeObject, for event id: {}, and type: {}", event.getId(), event.getType());
            return "ERROR_DESERIALIZING";
        }
        StripeObject stripeObject = dataObjectDeserializer.getObject().get();

        // For now, just handling customer.subscription.event
        if (!event.getType().equals(WebhookEventType.CUSTOMER_SUBSCRIPTION_UPDATED.name())) {
            return handleCustomerSubscriptionEvent(stripeObject);
        }

        return "UNHANDLED_EVENT_TYPE";
    }

    private String handleCustomerSubscriptionEvent(StripeObject stripeObject) {
        Subscription subscription = (Subscription) stripeObject;

        String subscriptionId = subscription.getId();
        String customerId = subscription.getCustomer();
        String status = subscription.getStatus();

        if (status.equalsIgnoreCase("active")) {
            log.info("{} Subscription is activated for subscriptionId: {}, customerId: {}", DEBUG_TAG, subscriptionId, customerId);
            return "SUCCESSFUL";
        }

        return "FAILED";
    }
}
