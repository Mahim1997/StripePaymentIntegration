# StripePaymentIntegration
Sample (Quick and Dirty) project for Stripe Payment Integration

## TODO
- Need to refactor the code (looks really bad now, just tested it out for functionality testing)
- Need to implement a webhook that will get called when payment is successfully completed
- Need one single API that will create the customer (if it doesn't exist) by taking an email, and perform checkout

## Very Important

In `resources/application.properties` file, add the Stripe Secret (private) key from your Stripe account.
This key will have a prefix of `sk_test_`
```
STRIPE_SECRET_KEY=<YOUR_SECRET_KEY>
```

## Some API calls (from POSTMAN)

1. Create Customer
```
POST
http://localhost:8080/api/payment/customer
{
    "name": "Test 2",
    "email": "test2_Sept22_5pm@gmail.com"
}
```
2. Create a Session (subscription/one-time): By default, I've used the subscription method inside `PaymentService.java`
```
POST
http://localhost:8080/api/payment/checkout-session
{
    "customerId": "cus_...",
    "priceId": "price_...",
    "currency": "aud",
    "quantity": 1
}

Returns
{
    "sessionId": "cs_test_.....",
    "stripeUrl": "https://checkout.stripe.com/c/pay/....."
}
```
Note:
- `customerId` will come from the customer object that we created earlier. We can use Stripe to access `customerId` via email (or store in our DB)
- `priceId` will come from storing it in DB. The price plans should already be created in our Stripe account.
- I have supplied both `successURL` and `cancelURL` inside the `Service` method (see first point for TODO about refactoring). This will be changed later in refactoring stage.

