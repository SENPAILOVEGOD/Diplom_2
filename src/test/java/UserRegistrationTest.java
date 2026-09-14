import clients.UserClient;
import helpers.UserAssertions;
import helpers.UserCreationResult;
import helpers.UserTestHelper;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;

@Epic("Stellar Burgers API")
@Feature("Создание пользователя")
public class UserRegistrationTest {

    private UserClient userClient;
    private UserTestHelper userTestHelper;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userTestHelper = new UserTestHelper(userClient);
    }

    @After
    public void tearDown() {
        userTestHelper.cleanUp();
    }

    @Test
    @Story("Создание уникального пользователя")
    @DisplayName("POST /api/auth/register Успешное создание уникального пользователя")
    @Description("Можно создать нового пользователя с уникальными данными")
    public void testCreateUniqueUserSuccess() {
        UserCreationResult result = userTestHelper.createUniqueUser("password123", "Username");
        UserAssertions.assertSuccess(result.getResponse());
    }

    @Test
    @Story("Создание уже зарегистрированного пользователя")
    @DisplayName("POST /api/auth/register Создание уже зарегистрированного пользователя")
    @Description("Повторная регистрация возвращает 403 Forbidden")
    public void testCreateAlreadyRegisteredUser() {
        UserCreationResult existing = userTestHelper.createUniqueUser("password123", "Username");

        Response duplicate = userTestHelper.createDuplicateUser(existing.getUser());
        UserAssertions.assertError(duplicate, SC_FORBIDDEN, "User already exists");
    }


    @Test
    @Story("Создание пользователя без email")
    @DisplayName("POST /api/auth/register Cоздание пользователя без email")
    @Description("Отсутствие email возвращает 403 Forbidden")
    public void testCreateUserWithoutEmail() {
        Response response = userTestHelper.createUserWithoutEmail();
        UserAssertions.assertError(response, SC_FORBIDDEN,
                "Email, password and name are required fields");
    }

    @Test
    @Story("Создание пользователя без пароля")
    @DisplayName("POST /api/auth/register Cоздание пользователя без password")
    @Description("Отсутствие password возвращает 403 Forbidden")
    public void testCreateUserWithoutPassword() {
        Response response = userTestHelper.createUserWithoutPassword();
        UserAssertions.assertError(response, SC_FORBIDDEN,
                "Email, password and name are required fields");
    }

    @Test
    @Story("Создание пользователя без имени")
    @DisplayName("POST /api/auth/register Cоздание пользователя без name")
    @Description("Отсутствие name возвращает 403 Forbidden")
    public void testCreateUserWithoutName() {
        Response response = userTestHelper.createUserWithoutName();
        UserAssertions.assertError(response, SC_FORBIDDEN,
                "Email, password and name are required fields");
    }
}