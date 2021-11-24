package kitchenpos.integration.api;

import java.util.List;
import kitchenpos.integration.api.texture.ProductTexture;
import kitchenpos.product.ui.request.ProductCreateRequest;
import kitchenpos.product.response.ProductResponse;
import kitchenpos.testtool.MockMvcResponse;
import kitchenpos.testtool.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductApi {

    private static final String BASE_URL = "/api/products";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<ProductResponse> 상품_등록(ProductTexture productTexture) {
        return 상품_등록(productTexture.getProduct());
    }

    public MockMvcResponse<ProductResponse> 상품_등록(ProductCreateRequest product) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(product)
            .asSingleResult(ProductResponse.class);
    }

    public MockMvcResponse<List<ProductResponse>> 상품_검색() {
        return mockMvcUtils.request()
            .get(BASE_URL)
            .asMultiResult(ProductResponse.class);
    }
}