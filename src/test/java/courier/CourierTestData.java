package courier;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CourierTestData {
    private String login;
    private String password;
    private String firstName;

    public static CourierTestData getRandomCourier() {
        String uuid = UUID.randomUUID().toString();
        return new CourierTestData(
                "courier_" + uuid.substring(0, 8),
                "password_" + uuid.substring(8, 16),
                "name_" + uuid.substring(16, 24)
        );
    }
}