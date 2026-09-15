package clients;

import io.restassured.response.Response;
import models.Order;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/orders";
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    public Response createOrder(Order order) {
        return baseSpec().body(order).post(ORDERS_PATH);
    }

    public Response createOrder(Order order, String accessToken) {
        return authSpec(accessToken).body(order).post(ORDERS_PATH);
    }

    public Response getIngredients() {
        return baseSpec().get(INGREDIENTS_PATH);
    }
}