package com.stripeintegration.sampleproject.model;

public enum WebhookEventType {
    PAYMENT_METHOD_ATTACHED("payment_method.attached"),
    CHARGE_SUCCEEDED("charge.succeeded"),
    PAYMENT_INTENT_SUCCEEDED("payment_intent.succeeded"),
    CHECKOUT_SESSION_COMPLETED("checkout.session.completed"),
    INVOICE_CREATED("invoice.created"),
    INVOICE_FINALIZED("invoice.finalized"),
    INVOICE_UPDATED("invoice.updated"),
    CUSTOMER_SUBSCRIPTION_CREATED("customer.subscription.created"),
    INVOICE_PAID("invoice.paid"),
    INVOICE_PAYMENT_SUCCEEDED("invoice.payment_succeeded"),
    PAYMENT_INTENT_CREATED("payment_intent.created"),
    CUSTOMER_SUBSCRIPTION_UPDATED("customer.subscription.updated");

    private final String eventTypeLowerCased;

    WebhookEventType(String eventType) {
        this.eventTypeLowerCased = eventType;
    }

    public String getEventTypeLowerCased() {
        return this.eventTypeLowerCased;
    }

    // Static method to get enum from string
    public static WebhookEventType fromString(String text) {
        for (WebhookEventType b : WebhookEventType.values()) {
            if (b.eventTypeLowerCased.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
