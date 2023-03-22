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

public class CreateCourierTest {
    private ScootersTestClient client;
    private ScooterTestSteps steps;

    @Before
    public void setUp() {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new ScootersTestClient();
        steps = new ScooterTestSteps(client);
    }

    @Test
    @DisplayName("Check that courier can be created")
    public void createCourierGreenFlowTest() {
        client.createCourier(Couriers.LOGIN, Couriers.PASSWORD, Couriers.FIRST_NAME)
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check that couriers with identical logins can't be created")
    public void createCouriersWithSameLoginReturnsAnError() {
        steps.createCourier(Couriers.LOGIN, Couriers.PASSWORD, Couriers.FIRST_NAME);
        client.createCourier(Couriers.LOGIN, Couriers.ANOTHER_PASSWORD, Couriers.ANOTHER_FIRST_NAME)
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check that two identical couriers can't be created")
    public void createTwoIdenticalCouriersReturnsAnError() {
        steps.createCourier(Couriers.LOGIN, Couriers.PASSWORD, Couriers.FIRST_NAME);
        client.createCourier(Couriers.LOGIN, Couriers.PASSWORD, Couriers.FIRST_NAME)
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check that courier without password can't be created")
    public void createCourierWithoutPasswordReturnsAnError() {
        client.createCourier(Couriers.LOGIN, null, Couriers.FIRST_NAME)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check that courier without login can't be created")
    public void createCourierWithoutLoginReturnsAnError() {
        client.createCourier(null, Couriers.PASSWORD, Couriers.FIRST_NAME)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check that courier without first name can be created")
    public void createCourierWithoutFirstNameIsOk() {
        client.createCourier(Couriers.LOGIN, Couriers.PASSWORD, null)
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @After
    public void tearDown() {
        steps.deleteCourierIfExists(Couriers.LOGIN, Couriers.PASSWORD);
    }
}
