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
    // Создание курьера перед каждым тестом
    public void setUp() {
        super.setUp();
        validCourier = CourierTestData.getRandomCourier();
        Response createResponse = CourierUtils.createCourier(validCourier);
        createResponse.then().statusCode(SC_CREATED);
    }

    @After
    // Очистка: удаление созданного курьера после каждого теста
    public void tearDown() {
        if (createdCourierId != null) {
            Response deleteResponse = CourierUtils.deleteCourier(createdCourierId);
            deleteResponse.then().statusCode(SC_OK); // Добавлена проверка успешного удаления
        }
    }

    @Test
    // Проверка успешной авторизации с валидными учетными данными
    public void testCourierLoginSuccessfully() {
        Response response = CourierUtils.loginCourier(validCourier);
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());

        createdCourierId = response.path("id").toString();
    }

    @Test
    // Проверка входа с неверным паролем (но существующим логином)
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

        // Получаем ID для последующего удаления
        Response loginResponse = CourierUtils.loginCourier(validCourier);
        createdCourierId = loginResponse.path("id").toString();
    }

    @Test
    // Проверка авторизации с несуществующими учетными данными
    public void testLoginWithNonExistentUser() {
        CourierTestData nonExistentCourier = CourierTestData.getRandomCourier();
        Response response = CourierUtils.loginCourier(nonExistentCourier);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    // Проверка авторизации без указания логина (только пароль)
    public void testLoginWithoutLogin() {
        Response response = CourierUtils.loginCourier(
                new CourierTestData(null, validCourier.getPassword(), null));
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    // Проверка авторизации без указания пароля (только логин)
    public void testLoginWithoutPassword() {
        Response response = CourierUtils.loginCourier(
                new CourierTestData(validCourier.getLogin(), null, null));
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}