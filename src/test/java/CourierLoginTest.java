import courier.CourierTestData;
import courier.CourierUtils;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends TestBase {

    @Test
    public void testCourierLoginSuccessfully() {
        CourierTestData courier = CourierTestData.getRandomCourier();
        CourierUtils.createCourier(courier);

        Response response = CourierUtils.loginCourier(courier);
        response.then()
                .statusCode(200)
                .body("id", notNullValue());

        // Cleanup
        String courierId = response.path("id").toString();
        CourierUtils.deleteCourier(courierId);
    }

    @Test
    public void testLoginWithIncorrectPassword() {
        CourierTestData courier = CourierTestData.getRandomCourier();
        CourierUtils.createCourier(courier);

        CourierTestData wrongCredentials = new CourierTestData(
                courier.getLogin(),
                "wrongPassword",
                null
        );

        Response response = CourierUtils.loginCourier(wrongCredentials);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));

        // Cleanup
        String courierId = CourierUtils.loginCourierAndGetId(courier);
        CourierUtils.deleteCourier(courierId);
    }

    @Test
    public void testLoginWithNonExistentUser() {
        CourierTestData nonExistentCourier = CourierTestData.getRandomCourier();

        Response response = CourierUtils.loginCourier(nonExistentCourier);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void testLoginWithoutRequiredField() {
        CourierTestData courier = new CourierTestData("loginOnly", null, "firstNameOnly");

        Response response = CourierUtils.loginCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}