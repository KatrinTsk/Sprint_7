import order.OrderTestData;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static order.OrderUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreationTest extends TestBase {
    private final OrderTestData order;
    private final String description;
    private Integer trackId; // Добавлено поле для хранения номера трека

    public OrderCreationTest(OrderTestData order, String description) {
        this.order = order;
        this.description = description;
    }

    // Параметризованные данные с разными цветами самоката
    @Parameterized.Parameters(name = "Цвета самоката: {1}")
    public static Object[][] getOrderData() {
        return new Object[][]{
                {OrderTestData.getOrderWithColor(List.of("BLACK")), "чёрный жемчуг"},
                {OrderTestData.getOrderWithColor(List.of("GREY")), "серая безысходность"},
                {OrderTestData.getOrderWithColor(List.of("BLACK", "GREY")), "чёрный жемчуг + серая безысходность"},
                {OrderTestData.getOrderWithColor(null), "цвет не указан"}
        };
    }

    @Test
    // Проверка создания заказа с разными вариантами цветов
    public void testCreateOrderWithDifferentColorOptions() {
        Response response = createOrder(order);
        trackId = response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract().path("track"); // Сохраняем номер трека
    }

    @After
    // Отмена созданного заказа
    public void tearDown() {
        if (trackId != null) {
            try {
                cancelOrder(trackId).then().statusCode(SC_OK);
            } catch (Exception e) {
                System.err.println("Ошибка при отмене заказа " + trackId + ": " + e.getMessage());
            }
        }
    }
}