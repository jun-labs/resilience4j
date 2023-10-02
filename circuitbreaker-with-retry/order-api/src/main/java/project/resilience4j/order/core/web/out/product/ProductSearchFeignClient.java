package project.resilience4j.order.core.web.out.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.resilience4j.order.core.web.dto.response.ProductResponse;

@FeignClient(url = "${url.product-server}", name = "ProductFeignClient")
public interface ProductSearchFeignClient {

    @GetMapping("/{productId}")
    ProductResponse findProduct(@PathVariable("productId") Long productId);
}
