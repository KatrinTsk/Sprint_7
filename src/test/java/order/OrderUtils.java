package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderUtils {
    @Step("Send POST request to /api/v1/orders")
    public static Response createOrder(OrderTestData order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Send GET request to /api/v1/orders")
    public static Response getOrdersList() {
        return given()
                .get("/api/v1/orders");
    }

    @Step("Отменить заказ")
    public static Response cancelOrder(int trackId) {
        return given()
                .queryParam("track", trackId)
                .put("/api/v1/orders/cancel");
    }
}