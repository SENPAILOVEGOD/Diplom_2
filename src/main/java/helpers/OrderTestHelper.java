package helpers;

import clients.OrderClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import java.util.List;

public class OrderTestHelper {

    public static final String VALID_BUN_ID   = "61c0c5a71d1f82001bdaaa6d"; // Флюоресцентная булка R2-D3
    public static final String VALID_MAIN_ID  = "61c0c5a71d1f82001bdaaa6f"; // Мясо бессмертных моллюсков
    public static final String VALID_SAUCE_ID = "61c0c5a71d1f82001bdaaa72"; // Соус Spicy-X

    public static final String INVALID_INGREDIENT_ID = "invalid_ingredient_hash";

    private final OrderClient orderClient;

    public OrderTestHelper(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @Step("Создаем заказ с авторизацией с ингредиентами")
    public Response createOrderWithAuth(String accessToken, List<String> ingredientIds) {
        Order order = Order.builder().ingredients(ingredientIds).build();
        return orderClient.createOrder(order, accessToken);
    }

    @Step("Создаем заказ без авторизации с ингредиентами")
    public Response createOrderWithoutAuth(List<String> ingredientIds) {
        Order order = Order.builder().ingredients(ingredientIds).build();
        return orderClient.createOrder(order);
    }

    @Step("Создаем заказ с авторизацией без ингредиентов")
    public Response createOrderWithoutIngredients(String accessToken) {
        Order order = Order.builder().ingredients(null).build();
        return orderClient.createOrder(order, accessToken);
    }

    @Step("Создаем заказ с авторизацией и невалидным хешем ингредиента")
    public Response createOrderWithInvalidIngredient(String accessToken, String ingredientId) {
        Order order = Order.builder().ingredients(List.of(ingredientId)).build();
        return orderClient.createOrder(order, accessToken);
    }
}