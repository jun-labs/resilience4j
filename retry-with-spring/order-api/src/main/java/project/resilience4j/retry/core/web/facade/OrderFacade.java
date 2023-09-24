package project.resilience4j.retry.core.web.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.resilience4j.retry.core.core.order.entity.Order;
import project.resilience4j.retry.core.web.dto.request.OrderRequest;
import project.resilience4j.retry.core.web.application.service.OrderService;
import project.resilience4j.retry.core.web.out.ProductClient;
import project.resilience4j.retry.core.web.dto.response.ProductResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductClient productClient;
    private final OrderService orderService;

    public Long order(OrderRequest request) {
        log.info("[Order/Facade]----xx> Order Request: {}", request);
        ProductResponse response = productClient.findProductById(request.getProductId());
        Order newOrder = orderService.save(createOrder(request, response));
        return newOrder.getOrderId();
    }

    private Order createOrder(
        OrderRequest request,
        ProductResponse response
    ) {
        return new Order(
            request.getUserId(),
            request.getProductId(),
            response.getPrice()
        );
    }
}
