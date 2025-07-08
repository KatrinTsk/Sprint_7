import courier.CourierTestData;
import courier.CourierUtils;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreationTest extends TestBase {
    private CourierTestData courier;
    private String courierId;

    @After
    public void tearDown() {
        if (courier != null && courierId == null) {
            try {
                courierId = CourierUtils.loginCourierAndGetId(courier);
            } catch (Exception e) {
                System.out.println("Failed to login and get courier ID: " + e.getMessage());
                return;
            }
        }

        if (courierId != null) {
            try {
                CourierUtils.deleteCourier(courierId);
            } catch (Exception e) {
                System.out.println("Failed to delete courier: " + e.getMessage());
            }
        }
    }

    @Test
    // Успешное создание нового курьера
    public void testCreateCourierSuccessfully() {
        courier = CourierTestData.getRandomCourier();
        Response response = CourierUtils.createCourier(courier);

        response.then()
                .statusCode(SC_CREATED)
                .body("ok", is(true));
    }
    @Test
    // Ошибка при попытке создать двух одинаковых курьеров
    public void testCreateDuplicateCourier() {
        courier = CourierTestData.getRandomCourier();
        CourierUtils.createCourier(courier);

        Response response = CourierUtils.createCourier(courier);
        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));

        courierId = CourierUtils.loginCourierAndGetId(courier);
    }

    @Test
    // Создание курьера без логина
    public void testCreateCourierWithoutLogin() {
        courier = new CourierTestData(null, "Password", "FirstName");
        Response response = CourierUtils.createCourier(courier);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    // Создание курьера без пароля
    public void testCreateCourierWithoutPassword() {
        courier = new CourierTestData("login", null, "FirstName");
        Response response = CourierUtils.createCourier(courier);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    // Ошибка при попытке создать курьера с уже существующим логином
    public void testCreateCourierWithExistingLogin() {
        courier = CourierTestData.getRandomCourier();
        CourierUtils.createCourier(courier);

        CourierTestData secondCourier = new CourierTestData(
                courier.getLogin(),
                "differentPassword",
                "differentName"
        );

        Response response = CourierUtils.createCourier(secondCourier);
        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));

        courierId = CourierUtils.loginCourierAndGetId(courier);
    }
}