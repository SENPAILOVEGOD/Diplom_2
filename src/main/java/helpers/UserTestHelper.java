package helpers;

import clients.UserClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;

import java.util.UUID;

import static org.apache.http.HttpStatus.SC_OK;

public class UserTestHelper {

    private static final String DEFAULT_PASSWORD = "password123";
    private static final String DEFAULT_NAME = "Username";

    private final UserClient userClient;
    private String createdUserToken;

    public UserTestHelper(UserClient userClient) {
        this.userClient = userClient;
    }

    private String generateUniqueEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@yandex.ru";
    }

    @Step("Создаем уникального пользователя")
    public UserCreationResult createUniqueUser(String password, String name) {
        User user = User.builder()
                .email(generateUniqueEmail())
                .password(password)
                .name(name)
                .build();

        Response response = userClient.register(user);
        response.then().statusCode(SC_OK);

        String token = response.path("accessToken");
        this.createdUserToken = token;

        return UserCreationResult.builder()
                .user(user)
                .response(response)
                .accessToken(token)
                .build();
    }

    @Step("Повторно регистрируем уже существующего пользователя")
    public Response createDuplicateUser(User existingUser) {
        return userClient.register(existingUser);
    }

    @Step("Создаем пользователя без email (заполнены password и name)")
    public Response createUserWithoutEmail() {
        User user = User.builder()
                .password(DEFAULT_PASSWORD)
                .name(DEFAULT_NAME)
                .build();
        return userClient.register(user);
    }

    @Step("Создаем пользователя без password (заполнены email и name)")
    public Response createUserWithoutPassword() {
        User user = User.builder()
                .email(generateUniqueEmail())
                .name(DEFAULT_NAME)
                .build();
        return userClient.register(user);
    }

    @Step("Создаем пользователя без name (заполнены email и password)")
    public Response createUserWithoutName() {
        User user = User.builder()
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .build();
        return userClient.register(user);
    }

    @Step("Удаляем созданного пользователя")
    public void cleanUp() {
        try {
            if (createdUserToken != null && !createdUserToken.isEmpty()) {
                userClient.deleteUser(createdUserToken);
            }
        } finally {
            createdUserToken = null;
        }
    }
}