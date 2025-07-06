import courier.CourierTestData;
import courier.CourierUtils;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends TestBase {
    private CourierTestData validCourier;
    private String createdCourierId;

    @Before
    @Override
    //Создание курьера
    public void setUp() {
        super.setUp();
        validCourier = CourierTestData.getRandomCourier();
        Response createResponse = CourierUtils.createCourier(validCourier);
        createResponse.then().statusCode(SC_CREATED);
    }

    @After
    // Очистка учетных данных курьера
    public void tearDown() {
        if (createdCourierId != null) {
            CourierUtils.deleteCourier(createdCourierId);
        }
    }

    @Test
    // Проверка успешной авторизации с валидными учетными данными
    public void testCourierLoginSuccessfully() {
        Response response = CourierUtils.loginCourier(validCourier);
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());

        // Сохраняем ID для очистки
        createdCourierId = response.path("id").toString();
    }

    @Test
    // Проверка входа с неверным паролем
    public void testLoginWithIncorrectPassword() {
        CourierTestData wrongCredentials = new CourierTestData(
                validCourier.getLogin(),
                "wrongPassword",
                null
        );
        Response response = CourierUtils.loginCourier(wrongCredentials);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));

        createdCourierId = CourierUtils.loginCourierAndGetId(validCourier);
    }

    @Test
    // Проверка авторизации несуществующего пользователя
    public void testLoginWithNonExistentUser() {
        CourierTestData nonExistentCourier = CourierTestData.getRandomCourier();
        Response response = CourierUtils.loginCourier(nonExistentCourier);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }


    @Test
    // Проверка авторизации без логина
    public void testLoginWithoutLogin() {
        Response response = CourierUtils.loginCourier(
                new CourierTestData(null, validCourier.getPassword(), null));
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    // Проверка авторизации без пароля
    public void testLoginWithoutPassword() {
        Response response = CourierUtils.loginCourier(
                new CourierTestData(validCourier.getLogin(), null, null));
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}