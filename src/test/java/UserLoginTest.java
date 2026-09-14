import clients.UserClient;
import helpers.TestDataGenerator;
import helpers.UserAssertions;
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

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@Epic("Stellar Burgers API")
@Feature("Авторизация пользователя")
public class UserLoginTest {

    private UserTestHelper userTestHelper;

    @Before
    public void setUp() {
        userTestHelper = new UserTestHelper(new UserClient());
    }

    @After
    public void tearDown() {
        userTestHelper.cleanUp();
    }

    @Test
    @Story("Вход под существующим пользователем")
    @DisplayName("POST /api/auth/login — успешная авторизация существующего пользователя")
    @Description("Успешный вход возвращает accessToken и данные пользователя")
    public void testLoginExistingUserSuccess() {
        UserCreationResult existingUser = userTestHelper.createUniqueUser();
        Response response = userTestHelper.loginAs(existingUser.getUser());
        UserAssertions.assertLoginSuccess(response, existingUser.getUser().getEmail());
    }

    @Test
    @Story("Вход с неверным логином и паролем")
    @DisplayName("POST /api/auth/login — авторизация с неверным логином и паролем")
    @Description("Неверные логин и пароль возвращают 401 Unauthorized")
    public void testLoginWithWrongEmailAndPassword() {
        String nonExistentEmail = TestDataGenerator.uniqueEmail();
        Response response = userTestHelper.loginWithInvalidCredentials(nonExistentEmail, "wrongPassword");
        UserAssertions.assertError(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }

    @Test
    @Story("Вход с неверным паролем")
    @DisplayName("POST /api/auth/login — авторизация существующего пользователя с неверным паролем")
    @Description("Неверный пароль возвращает 401 Unauthorized")
    public void testLoginExistingUserWrongPassword() {
        UserCreationResult existing = userTestHelper.createUniqueUser();

        Response response = userTestHelper.loginWithWrongPassword(
                existing.getUser().getEmail(),
                "wrongPassword"
        );

        UserAssertions.assertError(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }
}