package com.xenon.ecommerce.order;

import com.xenon.ecommerce.customer.CustomerClient;
import com.xenon.ecommerce.exception.BusinessException;
import com.xenon.ecommerce.orderLine.OrderLineRequest;
import com.xenon.ecommerce.orderLine.OrderLineService;
import com.xenon.ecommerce.product.ProductClient;
import com.xenon.ecommerce.product.PurchaseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;

    public Integer createOrder(@Valid OrderRequest request) {
        //Check the customer  --> openFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with ID:: "+ request.customerId()));

        //Purchase the product --> Product-ms (RestTemplate)
        this.productClient.purchaseProducts(request.products());

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


        //send the order confirmation --> notification-ms(kafka)



        return null;
    }
}
