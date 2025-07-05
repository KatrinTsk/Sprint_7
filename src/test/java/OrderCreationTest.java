import order.OrderTestData;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static order.OrderUtils.createOrder;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreationTest extends TestBase {
    private final OrderTestData order;

    public OrderCreationTest(OrderTestData order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {OrderTestData.getOrderWithColor(List.of("BLACK"))},
                {OrderTestData.getOrderWithColor(List.of("GREY"))},
                {OrderTestData.getOrderWithColor(List.of("BLACK", "GREY"))},
                {OrderTestData.getOrderWithColor(null)}
        };
    }

    @Test
    public void testCreateOrderWithDifferentColorOptions() {
        Response response = createOrder(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}