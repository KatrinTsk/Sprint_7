import io.restassured.response.Response;
import org.junit.Test;

import static order.OrderUtils.getOrdersList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;


public class OrderListTest extends TestBase {

    @Test
    // Проверка, что в тело ответа возвращается список заказов
    public void testGetOrdersListReturnsListOfOrders() {
        Response response = getOrdersList();
        response.then()
                .statusCode(SC_OK)
                .body("orders", not(empty()));
    }
}