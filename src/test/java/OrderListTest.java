import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static order.OrderUtils.getOrdersList;
import static org.hamcrest.Matchers.*;

public class OrderListTest extends TestBase {

    @Test
    public void testGetOrdersListReturnsListOfOrders() {
        Response response = getOrdersList();
        response.then()
                .statusCode(200)
                .body("orders", not(empty()));
    }
}