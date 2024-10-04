package com.xenon.ecommerce.kafka;

import com.xenon.ecommerce.customer.CustomerResponse;
import com.xenon.ecommerce.order.PaymentMethod;
import com.xenon.ecommerce.product.PurchaseRequest;
import com.xenon.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
