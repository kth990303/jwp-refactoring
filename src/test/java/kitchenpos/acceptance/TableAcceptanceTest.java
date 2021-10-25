package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/tables")
    @Test
    void create() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", "1");
        params.put("empty", false);

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("GET /api/tables")
    @Test
    void list() {
        // given

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("PUT /api/tables/{orderTableId}/empty")
    @Test
    void changeEmpty() {
        // given
        long orderTableId = POST_DEFAULT_ORDER_TABLE(1, false);

        Map<String, Object> params = new HashMap<>();
        params.put("empty", true);

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        OrderTable orderTable = response.as(OrderTable.class);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests")
    @Test
    void changeNumberOfGuests() {
        // given
        long orderTableId = POST_DEFAULT_ORDER_TABLE(1, false);

        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", "10");

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        OrderTable orderTable = response.as(OrderTable.class);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    public static long POST_DEFAULT_ORDER_TABLE(int numberOfGuests, boolean empty) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);
        params.put("empty", empty);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        OrderTable orderTable = response.as(OrderTable.class);
        return orderTable.getId();
    }
}