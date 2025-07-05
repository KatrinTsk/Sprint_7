import courier.CourierTestData;
import courier.CourierUtils;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierCreationTest extends TestBase {

    @Test
    public void testCreateCourierSuccessfully() {
        CourierTestData courier = CourierTestData.getRandomCourier();
        Response response = CourierUtils.createCourier(courier);

        response.then()
                .statusCode(201)
                .body("ok", is(true));

        // Cleanup
        String courierId = CourierUtils.loginCourierAndGetId(courier);
        CourierUtils.deleteCourier(courierId);
    }

    @Test
    public void testCreateDuplicateCourier() {
        CourierTestData courier = CourierTestData.getRandomCourier();
        CourierUtils.createCourier(courier);

        Response response = CourierUtils.createCourier(courier);
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));

        // Cleanup
        String courierId = CourierUtils.loginCourierAndGetId(courier);
        CourierUtils.deleteCourier(courierId);
    }

    @Test
    public void testCreateCourierWithoutRequiredField() {
        CourierTestData courier = new CourierTestData("loginOnly", null, null);
        Response response = CourierUtils.createCourier(courier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void testCreateCourierWithExistingLogin() {
        CourierTestData firstCourier = CourierTestData.getRandomCourier();
        CourierUtils.createCourier(firstCourier);

        CourierTestData secondCourier = new CourierTestData(
                firstCourier.getLogin(),
                "differentPassword",
                "differentName"
        );

        Response response = CourierUtils.createCourier(secondCourier);
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));

        // Cleanup
        String courierId = CourierUtils.loginCourierAndGetId(firstCourier);
        CourierUtils.deleteCourier(courierId);
    }
}