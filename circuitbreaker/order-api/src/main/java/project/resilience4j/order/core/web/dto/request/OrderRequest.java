package project.resilience4j.order.core.web.dto.request;

import lombok.Getter;

@Getter
public class OrderRequest {

    private Long userId;
    private Long productId;

    private OrderRequest() {
    }

    public OrderRequest(
        Long userId,
        Long productId
    ) {
        this.userId = userId;
        this.productId = productId;
    }

    @Override
    public String toString() {
        return String.format("userId: %s, productId: %s", userId, productId);
    }
}
