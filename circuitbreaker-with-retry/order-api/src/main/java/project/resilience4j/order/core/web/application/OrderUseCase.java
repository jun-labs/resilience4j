package project.resilience4j.order.core.web.application;

import project.resilience4j.order.core.core.order.entity.Order;

public interface OrderUseCase {

    Order save(Order order);
}
