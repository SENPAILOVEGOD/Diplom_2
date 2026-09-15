package helpers;

import io.restassured.response.Response;
import lombok.Builder;
import lombok.Getter;
import models.User;

@Getter
@Builder
public class UserCreationResult {
    private final User user;
    private final Response response;
    private final String accessToken;
}