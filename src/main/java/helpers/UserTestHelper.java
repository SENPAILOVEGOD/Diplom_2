package helpers;

import clients.UserClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;

import static org.apache.http.HttpStatus.SC_OK;

public class UserTestHelper {

    private static final String DEFAULT_PASSWORD = "password123";
    private static final String DEFAULT_NAME = "Username";

    private final UserClient userClient;
    private String createdUserToken;

    public UserTestHelper(UserClient userClient) {
        this.userClient = userClient;
    }


    @Step("Создаем уникального пользователя")
    public UserCreationResult createUniqueUser() {
        User user = User.builder()
                .email(TestDataGenerator.uniqueEmail())
                .password(DEFAULT_PASSWORD)
                .name(DEFAULT_NAME)
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
                .email(TestDataGenerator.uniqueEmail())
                .name(DEFAULT_NAME)
                .build();
        return userClient.register(user);
    }

    @Step("Создаем пользователя без name (заполнены email и password)")
    public Response createUserWithoutName() {
        User user = User.builder()
                .email(TestDataGenerator.uniqueEmail())
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

    @Step("Авторизуемся под существующим пользователем")
    public Response loginAs(User user) {
        return userClient.login(user);
    }

    @Step("Авторизуемся с несуществующими email и паролем")
    public Response loginWithInvalidCredentials(String email, String password) {
        User user = User.builder()
                .email(email)
                .password(password)
                .build();
        return userClient.login(user);
    }

    @Step("Авторизуемся с неверным паролем")
    public Response loginWithWrongPassword(String email, String password) {
        User user = User.builder()
                .email(email)
                .password(password)
                .build();
        return userClient.login(user);
    }
}