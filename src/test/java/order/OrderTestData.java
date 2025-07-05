package order;

import java.util.List;

public class OrderTestData {
    private List<String> color;

    public OrderTestData(List<String> color) {
        this.color = color;
    }

    // Добавьте геттер
    public List<String> getColor() {
        return color;
    }

    public static OrderTestData getOrderWithColor(List<String> color) {
        return new OrderTestData(color);
    }
}