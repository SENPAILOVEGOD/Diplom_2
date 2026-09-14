package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public final class UserAssertions {
    private UserAssertions() {}

    @Step("Проверяем успешный ответ")
    public static void assertSuccess(Response response) {
        response.then()
                .log().all()
                .body("success", equalTo(true));
    }

    @Step("Проверяем ответ с ошибкой")
    public static void assertError(Response response, int status, String message) {
        response.then()
                .log().all()
                .statusCode(status)
                .body("success", equalTo(false))
                .body("message", equalTo(message));
    }
}