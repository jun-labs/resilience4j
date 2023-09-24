package project.resilience4j.retry.core.core.order.entity;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Order {

    private Long orderId;
    private Long userId;
    private Long productId;
    private BigDecimal price;

    public Order(
        Long userId,
        Long productId,
        BigDecimal price
    ) {
        this.userId = userId;
        this.productId = productId;
        this.price = price;
    }

    public void putPk(Long newPk) {
        this.orderId = newPk;
    }

    @Override
    public String toString() {
        return String.valueOf(orderId);
    }
}
