package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierUtils {
    @Step("Send POST request to /api/v1/courier")
    public static Response createCourier(CourierTestData courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("Send POST request to /api/v1/courier/login")
    public static Response loginCourier(CourierTestData courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login");
    }

    @Step("Login courier and get id")
    public static String loginCourierAndGetId(CourierTestData courier) {
        Response response = loginCourier(courier);
        return response.path("id").toString();
    }

    @Step("Send DELETE request to /api/v1/courier/:id")
    public static Response deleteCourier(String id) {
        return given()
                .delete("/api/v1/courier/" + id);
    }
}