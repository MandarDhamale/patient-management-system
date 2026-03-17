import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:4004"; // API gateway address
        // this is going to work as if a client from real world is trying to access the application
    }

    @Test
    public void shouldReturnOKWithValidToken() {
        // 1. Arrange

        String loginPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": "password123"
                    }
                """;

        // 2. Act

        Response response = given().contentType("application/json").body(loginPayload).when().post("/auth/login").then().statusCode(200).body("token", notNullValue()).extract().response(); // extract method is going to pull the response from the request that we made up above and .response() is going to get the response object and assign to the response variable


        System.out.println("Generate token: " + response.jsonPath().getString("token"));


        // 3. Assert
    }

    // Negative test
    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        String loginPayload = """
                    {
                        "email": "invalid_test@test.com",
                        "password": "wrong_password"
                    }
                """;

        given().contentType("application/json").body(loginPayload).when().post("/auth/login").then().statusCode(401);
    }


}