package project.resilience4j.retry.core.web.out.search;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.resilience4j.retry.core.web.dto.response.ProductResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@FeignClient(url = "${url.product-server}", name = "ProductFeignClient")
public interface ProductSearchFeignClient {

    @GetMapping("/{productId}")
    ProductResponse findProduct(@PathVariable("productId") Long productId);
}
