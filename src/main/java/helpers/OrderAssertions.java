package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public final class OrderAssertions {

    private OrderAssertions() {}

    @Step("Проверяем, что заказ создан успешно")
    public static void assertOrderCreated(Response response) {
        assertStatusCode(response, SC_OK);
        assertSuccess(response, true);
        assertOrderNumber(response);
        assertOrderName(response);
    }

    @Step("Проверяем статус-код ответа")
    public static void assertStatusCode(Response response, int status) {
        response.then().log().all().statusCode(status);
    }

    @Step("Проверяем success в ответе")
    public static void assertSuccess(Response response, boolean expected) {
        response.then().body("success", equalTo(expected));
    }

    @Step("Проверяем, что заказ не был создан")
    public static void assertOrderNotCreated(Response response) {
        response.then().log().all();
        response.then().body("order", nullValue());
        response.then().body("success", not(equalTo(true)));
    }

    @Step("Проверяем, что в ответе есть номер заказа (order.number)")
    public static void assertOrderNumber(Response response) {
        response.then().body("order.number", notNullValue());
    }

    @Step("Проверяем, что в ответе есть имя заказа")
    public static void assertOrderName(Response response) {
        response.then().body("name", not(emptyOrNullString()));
    }

    @Step("Проверяем, что ингредиенты не переданы")
    public static void assertIngredientsRequired(Response response, String message) {
        assertStatusCode(response, SC_BAD_REQUEST);
        assertSuccess(response, false);
        response.then().body("message", equalTo(message));
    }

    @Step("Проверяем, что сервер вернул 500 Internal Server Error")
    public static void assertInternalServerError(Response response) {
        assertStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }
}