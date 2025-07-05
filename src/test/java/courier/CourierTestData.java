package courier;

import java.util.UUID;

public class CourierTestData {
    private String login;
    private String password;
    private String firstName;

    public CourierTestData(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    // Геттеры
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public static CourierTestData getRandomCourier() {
        String uuid = UUID.randomUUID().toString();
        return new CourierTestData(
                "courier_" + uuid.substring(0, 8),
                "password_" + uuid.substring(8, 16),
                "name_" + uuid.substring(16, 24)
        );
    }
}