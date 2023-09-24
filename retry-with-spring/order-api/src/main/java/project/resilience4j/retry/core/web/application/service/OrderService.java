package project.resilience4j.retry.core.web.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.resilience4j.retry.core.core.order.entity.Order;
import project.resilience4j.retry.core.core.order.repository.OrderRepository;
import project.resilience4j.retry.core.web.application.OrderUseCase;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        log.info("[Order/Service]----xx> Order Request: {}", order);
        return orderRepository.save(order);
    }
}
