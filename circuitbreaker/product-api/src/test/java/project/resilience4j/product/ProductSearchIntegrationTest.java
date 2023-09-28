package project.resilience4j.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.resilience4j.product.core.domain.product.entity.Product;
import project.resilience4j.product.core.web.application.ProductSearchUseCase;
import project.resilience4j.product.core.web.exception.ProductNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
class ProductSearchIntegrationTest {

    @Autowired
    private ProductSearchUseCase productSearchUseCase;

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("존재하는 상품은 PK로 조회할 수 있다.")
    void product_search_test(Long parameter) {
        Product findProduct = productSearchUseCase.findProductById(parameter);
        assertNotNull(findProduct);
    }

    @ParameterizedTest
    @ValueSource(longs = {4L, 5L, 6L})
    @DisplayName("존재하지 않는 상품을 조회하면 ProductNotFoundException이 발생한다.")
    void product_search_failure_test(Long parameter) {
        assertThatThrownBy(() -> productSearchUseCase.findProductById(parameter))
            .isExactlyInstanceOf(ProductNotFoundException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage("상품을 찾을 수 없습니다.");
    }
}
