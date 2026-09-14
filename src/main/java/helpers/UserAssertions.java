package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public final class UserAssertions {
    private UserAssertions() {}

    @Step("Проверяем статус код ответа")
    public static void assertStatusCode(Response response, int status) {
        response.then().log().all().statusCode(status);
    }

    @Step("Проверяем в ответе success")
    public static void assertSuccess(Response response, boolean expected) {
        response.then().body("success", equalTo(expected));
    }

    @Step("Проверяем, что accessToken присутствует и не пустой")
    public static void assertAccessTokenPresent(Response response) {
        response.then().body("accessToken", not(emptyOrNullString()));
    }

    @Step("Проверяем, что email в ответе совпадает с ожидаемым")
    public static void assertUserEmail(Response response, String expectedEmail) {
        response.then().body("user.email", equalTo(expectedEmail));
    }

    @Step("Проверяем сообщение об ошибке")
    public static void assertMessage(Response response, String message) {
        response.then().body("message", equalTo(message));
    }


    @Step("Проверяем ответ с ошибкой: статус, сообщение")
    public static void assertError(Response response, int status, String message) {
        assertStatusCode(response, status);
        assertSuccess(response, false);
        assertMessage(response, message);
    }

    @Step("Проверяем успешный ответ авторизации")
    public static void assertLoginSuccess(Response response, String expectedEmail) {
        assertStatusCode(response, SC_OK);
        assertSuccess(response, true);
        assertAccessTokenPresent(response);
        assertUserEmail(response, expectedEmail);
    }
}