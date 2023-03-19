package client;

import dto.CourierCreateRequestDto;
import dto.CourierLoginRequestDto;
import dto.OrderCreateRequestDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ScootersTestClient {

    @Step("GET /api/v1/courier")
    public ValidatableResponse createCourier(String login, String password, String firstName) {
        CourierCreateRequestDto requestDto = new CourierCreateRequestDto(login, password, firstName);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestDto)
                .log()
                .all()
                .when()
                .post("/api/v1/courier")
                .then()
                .log()
                .all();
    }

    @Step("POST /api/v1/courier/login")
    public ValidatableResponse courierLogin(String login, String password) {
        CourierLoginRequestDto requestDto = new CourierLoginRequestDto(login, password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestDto)
                .log()
                .all()
                .when()
                .post("/api/v1/courier/login")
                .then()
                .log()
                .all();
    }

    @Step("DELETE /api/v1/courier/:id")
    public ValidatableResponse courierDelete(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .pathParam("id", courierId)
                .and()
                .log().all()
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .log().all();
    }

    @Step("POST /api/v1/orders")
    public ValidatableResponse orderCreate(OrderCreateRequestDto requestDto) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestDto)
                .log().all()
                .when()
                .post("/api/v1/orders")
                .then()
                .log().all();
    }

    @Step("GET /api/v1/orders/track?t=<trackId>")
    public ValidatableResponse orderGet(int trackId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .params("t", trackId)
                .log().all()
                .when()
                .get("/api/v1/orders/track")
                .then()
                .log().all();
    }

    @Step("PUT /api/v1/orders/accept/:id?courierId=<courierId>")
    public ValidatableResponse orderAccept(int orderId, int courierId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .pathParams("id", orderId)
                .params("courierId", courierId)
                .log().all()
                .when()
                .put("/api/v1/orders/accept/{id}")
                .then()
                .log().all();
    }

    @Step("GET /api/v1/orders")
    public ValidatableResponse listOrders(Integer courierId, String nearestStation, Integer limit, Integer page) {
        RequestSpecification request = given()
                .header("Content-type", "application/json")
                .and();
        if (courierId != null) {
            request = request
                    .param("courierId", courierId)
                    .and();
        }
        if (nearestStation != null) {
            request = request
                    .param("nearestStation", "[\"" + nearestStation + "\"]")
                    .and();
        }
        if (limit != null) {
            request = request
                    .param("limit", limit)
                    .and();
        }
        if (page != null) {
            request = request
                    .param("page", page)
                    .and();
        }
        return request
                .log().all()
                .when()
                .get("/api/v1/orders")
                .then()
                .log().all();
    }
}
