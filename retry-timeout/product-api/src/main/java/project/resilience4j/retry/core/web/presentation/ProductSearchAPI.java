package project.resilience4j.retry.core.web.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.resilience4j.retry.common.exception.RequestTimeoutException;
import project.resilience4j.retry.core.domain.product.entity.Product;
import project.resilience4j.retry.core.web.application.service.ProductSearchService;
import project.resilience4j.retry.core.web.dto.ProductResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductSearchAPI {

    private final ProductSearchService productSearchService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(
            @PathVariable("productId") Long productId
    ) throws InterruptedException {
        if (productId >= 500 & productId % 2 == 0) {
            Thread.sleep(5000);
            throw new RequestTimeoutException();
        }
        log.info("[Product/Presentation]----xx> Search Request: {}", productId);
        Product findProduct = productSearchService.findProductById(productId);
        return ResponseEntity.ok(new ProductResponse(findProduct));
    }
}
