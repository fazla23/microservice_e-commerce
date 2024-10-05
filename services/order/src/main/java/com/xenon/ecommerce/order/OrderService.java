package com.xenon.ecommerce.order;

import com.xenon.ecommerce.customer.CustomerClient;
import com.xenon.ecommerce.exception.BusinessException;
import com.xenon.ecommerce.kafka.OrderConfirmation;
import com.xenon.ecommerce.kafka.OrderProducer;
import com.xenon.ecommerce.orderLine.OrderLineRequest;
import com.xenon.ecommerce.orderLine.OrderLineService;
import com.xenon.ecommerce.payment.PaymentClient;
import com.xenon.ecommerce.payment.PaymentRequest;
import com.xenon.ecommerce.product.ProductClient;
import com.xenon.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(@Valid OrderRequest request) {
        //Check the customer  --> openFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with ID:: "+ request.customerId()));

        //Purchase the product --> Product-ms (RestTemplate)
        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        //persist order
        var order = this.orderRepository.save(mapper.toOrder(request));

        //persist order lines
        for(PurchaseRequest purchaseRequest:request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }


        //start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);


        //send the order confirmation --> notification-ms(kafka)

        orderProducer.sendOrderConfirmation(new OrderConfirmation(
                request.reference(), request.amount(), request.paymentMethod(), customer, purchasedProducts
        ));

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(mapper::toOrderResponse)
                .toList();
    }

    public OrderResponse findOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::toOrderResponse)
                .orElseThrow(()-> new EntityNotFoundException("No order found with ID:: "+ orderId));
    }
}
