package project.resilience4j.retry.test.documentationtest;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

// 문서화는 제외
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("[DocumentationTest] 상품 조회 통합 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductSearchDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(longs = {502, 504, 506, 508, 1000})
    @DisplayName("PK가 500이 넘어가는 값 중 짝수는 502 RequestTimeoutException이 반환한다.")
    void product_search_request_timeout_test(Long parameter) throws Exception {
        mockMvc.perform(get("/api/products/{productId}", parameter))
                .andExpect(status().is(408));
    }
}
