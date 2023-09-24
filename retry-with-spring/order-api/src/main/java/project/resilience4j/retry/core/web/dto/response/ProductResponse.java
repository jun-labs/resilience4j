package project.resilience4j.retry.core.web.dto.response;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String title;
    private BigDecimal price;

    private ProductResponse() {
    }

    public ProductResponse(
        Long id,
        String title,
        BigDecimal price
    ) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
