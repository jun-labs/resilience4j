package project.resilience4j.order.core.core.order.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.resilience4j.order.core.core.order.entity.Order;

@Slf4j
@Repository
public class OrderRepository {

    private static final AtomicLong PK_GENERATOR = new AtomicLong(0L);
    private final Map<Long, Order> factory;

    public OrderRepository() {
        this.factory = new HashMap<>();
    }

    public Order save(Order order) {
        log.info("[Order/Repository]----xx> Order Request: {}", order);
        Long newPk = PK_GENERATOR.incrementAndGet();
        order.putPk(newPk);
        factory.put(PK_GENERATOR.incrementAndGet(), order);
        return order;
    }
}
