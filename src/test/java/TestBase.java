import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;

public class TestBase {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
}
