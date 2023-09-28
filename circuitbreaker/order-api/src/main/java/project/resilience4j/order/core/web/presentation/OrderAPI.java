package project.resilience4j.order.core.web.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.resilience4j.order.core.web.dto.request.OrderRequest;
import project.resilience4j.order.core.web.dto.response.OrderResponse;
import project.resilience4j.order.core.web.facade.OrderFacade;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderAPI {

    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
        log.info("[Order/Presentation]----xx> Order Request: {}", request);
        Long newOrderId = orderFacade.order(request);
        return ResponseEntity.ok(new OrderResponse(newOrderId));
    }
}
