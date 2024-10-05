package com.xenon.ecommerce.payment;

import com.xenon.ecommerce.customer.CustomerResponse;
import com.xenon.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}