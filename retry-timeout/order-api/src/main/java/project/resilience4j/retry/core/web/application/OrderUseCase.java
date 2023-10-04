package project.resilience4j.retry.core.web.application;

import project.resilience4j.retry.core.core.order.entity.Order;

public interface OrderUseCase {

    Order save(Order order);
}
