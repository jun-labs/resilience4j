package project.resilience4j.retry.core.web.dto.response;

import lombok.Getter;

@Getter
public class OrderResponse {

    private final Long orderId;

    public OrderResponse(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return orderId.toString();
    }
}
