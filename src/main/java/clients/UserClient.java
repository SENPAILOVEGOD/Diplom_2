package clients;

import io.restassured.response.Response;
import models.User;

public class UserClient extends BaseClient {

    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user";
    private static final String LOGOUT_PATH = "/api/auth/logout";

    public Response register(User user) {
        return baseSpec().body(user).post(REGISTER_PATH);
    }

    public Response login(User user) {
        return baseSpec().body(user).post(LOGIN_PATH);
    }

    public Response getUser(String accessToken) {
        return authSpec(accessToken).get(USER_PATH);
    }

    public Response updateUser(String accessToken, User user) {
        return authSpec(accessToken).body(user).patch(USER_PATH);
    }

    public Response deleteUser(String accessToken) {
        return authSpec(accessToken).delete(USER_PATH);
    }

    public Response logout(String accessToken) {
        return authSpec(accessToken).post(LOGOUT_PATH);
    }
}