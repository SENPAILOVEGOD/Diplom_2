package clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseClient {

    protected static final String BASE_URI = "https://stellarburgers.education-services.ru";

    static {
        RestAssured.baseURI = BASE_URI;
        RestAssured.filters(new AllureRestAssured());
    }

    protected RequestSpecification baseSpec() {
        return RestAssured.given()
                .contentType(ContentType.JSON);
    }

    protected RequestSpecification authSpec(String accessToken) {
        return baseSpec().header("Authorization", accessToken);
    }
}