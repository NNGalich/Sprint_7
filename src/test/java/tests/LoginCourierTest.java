package tests;

import client.ScootersTestClient;
import constants.Couriers;
import constants.ServerUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.ScooterTestSteps;

import static org.hamcrest.Matchers.*;

public class LoginCourierTest {

    private ScootersTestClient client;
    private ScooterTestSteps steps;

    @Before
    public void setUp() {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new ScootersTestClient();
        steps = new ScooterTestSteps(client);

        steps.createCourier(Couriers.LOGIN, Couriers.PASSWORD, Couriers.FIRST_NAME);
    }

    @Test
    @DisplayName("Check that courier can log in")
    public void loginCourierGreenFlowTest() {
        client.courierLogin(Couriers.LOGIN, Couriers.PASSWORD)
                .statusCode(200)
                .body("id", any(Integer.class));
    }

    @Test
    @DisplayName("Check that unknown login returns 404")
    public void loginCourierWithWrongLoginReturnsAnError() {
        client.courierLogin(Couriers.ANOTHER_LOGIN, Couriers.PASSWORD)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check that invalid password returns 404")
    public void loginCourierWithWrongPasswordReturnsAnError() {
        client.courierLogin(Couriers.LOGIN, Couriers.ANOTHER_PASSWORD)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check that login without login field returns 400")
    public void loginCourierWithoutLoginReturnsAnError() {
        client.courierLogin(null, Couriers.PASSWORD)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check that login without password field returns 400")
    public void loginCourierWithoutPasswordReturnsAnError() {
        client.courierLogin(Couriers.LOGIN, null)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check that login with non-existing login/password pair returns 404")
    public void loginCourierWithNotExistingUserReturnsAnError() {
        client.courierLogin(Couriers.ANOTHER_LOGIN, Couriers.ANOTHER_PASSWORD)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        steps.deleteCourierIfExists(Couriers.LOGIN, Couriers.PASSWORD);
    }
}
