package tests;

import clients.OrderClient;
import clients.UserClient;
import helpers.OrderAssertions;
import helpers.OrderTestHelper;
import helpers.UserCreationResult;
import helpers.UserTestHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Epic("Stellar Burgers API")
@Feature("Создание заказа")
public class OrderCreateTest {

    private UserTestHelper userTestHelper;
    private OrderTestHelper orderTestHelper;
    private UserCreationResult currentUser;

    @Before
    public void setUp() {
        userTestHelper = new UserTestHelper(new UserClient());
        orderTestHelper = new OrderTestHelper(new OrderClient());
        currentUser = userTestHelper.createUniqueUser();
    }

    @After
    public void tearDown() {
        userTestHelper.cleanUp();
    }

    @Test
    @Story("Создание заказа с авторизацией")
    @DisplayName("POST /api/orders Заказ авторизованным пользователем")
    @Description("Авторизованный пользователь может создать заказ с валидными ингредиентами")
    public void testCreateOrderWithAuth() {
        Response response = orderTestHelper.createOrderWithAuth(
                currentUser.getAccessToken(),
                List.of(OrderTestHelper.VALID_BUN_ID, OrderTestHelper.VALID_MAIN_ID)
        );

        OrderAssertions.assertOrderCreated(response);
    }

    @Test
    @Story("Создание заказа без авторизации")
    @DisplayName("POST /api/orders Заказ без авторизации не создаётся")
    @Description("Неавторизованный запрос не приводит к созданию заказа: в ответе нет order")
    public void testCreateOrderWithoutAuth() {
        Response response = orderTestHelper.createOrderWithoutAuth(
                List.of(OrderTestHelper.VALID_BUN_ID, OrderTestHelper.VALID_MAIN_ID)
        );

        OrderAssertions.assertOrderNotCreated(response);
    }

    @Test
    @Story("Создание заказа с ингредиентами")
    @DisplayName("POST /api/orders Создание заказа с полным набором ингредиентов")
    @Description("Заказ с булкой, начинкой и соусом создаётся успешно")
    public void testCreateOrderWithIngredients() {
        Response response = orderTestHelper.createOrderWithAuth(
                currentUser.getAccessToken(),
                List.of(
                        OrderTestHelper.VALID_BUN_ID,
                        OrderTestHelper.VALID_MAIN_ID,
                        OrderTestHelper.VALID_SAUCE_ID
                )
        );

        OrderAssertions.assertOrderCreated(response);
    }

    @Test
    @Story("Создание заказа без ингредиентов")
    @DisplayName("POST /api/orders Создание заказа без ингредиентов")
    @Description("Запрос без ингредиентов возвращает 400 Bad Request")
    public void testCreateOrderWithoutIngredients() {
        Response response = orderTestHelper.createOrderWithoutIngredients(currentUser.getAccessToken());

        OrderAssertions.assertIngredientsRequired(response, "Ingredient ids must be provided");
    }

    @Test
    @Story("Создание заказа с неверным хешем ингредиентов")
    @DisplayName("POST /api/orders Создание заказа с невалидным хешем ингредиента")
    @Description("Невалидный хеш ингредиента возвращает 500 Internal Server Error")
    public void testCreateOrderWithInvalidIngredientHash() {
        Response response = orderTestHelper.createOrderWithInvalidIngredient(
                currentUser.getAccessToken(),
                OrderTestHelper.INVALID_INGREDIENT_ID
        );

        OrderAssertions.assertInternalServerError(response);
    }
}