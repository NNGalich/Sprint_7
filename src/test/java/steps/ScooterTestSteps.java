package steps;

import java.util.List;

import client.ScootersTestClient;
import dto.CourierLoginResponseDto;
import dto.OrderCreateRequestDto;
import dto.OrderCreateResponseDto;
import dto.OrderGetOrderDto;
import dto.OrderGetResponseDto;
import dto.OrdersListResponseDto;
import io.qameta.allure.Step;

import static org.hamcrest.Matchers.equalTo;

public class ScooterTestSteps {
    private final ScootersTestClient client;

    public ScooterTestSteps(ScootersTestClient client) {
        this.client = client;
    }

    @Step("Create courier")
    public void createCourier(String login, String password, String firstName) {
        client.createCourier(login, password, firstName)
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Login to courier and get id")
    public int loginCourierAndGetId(String login, String password) {
        return client.courierLogin(login, password)
                .statusCode(200)
                .extract()
                .as(CourierLoginResponseDto.class)
                .getId();
    }

    @Step("Delete courier by id")
    private void deleteCourierInternal(int courierId) {
        client.courierDelete(courierId)
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Step("Delete courier if present")
    public void deleteCourierIfExists(String login, String password) {
        try {
            int id = loginCourierAndGetId(login, password);
            deleteCourierInternal(id);
        } catch (AssertionError e) {
        }
    }

    @Step("Get order by track id")
    public OrderGetOrderDto getOrderByTrackId(int trackId) {
        return client.orderGet(trackId)
                .statusCode(200)
                .extract()
                .as(OrderGetResponseDto.class)
                .getOrder();
    }

    @Step("Accept order to courier")
    public void acceptOrderToCourier(int orderId, int courierId) {
        client.orderAccept(orderId, courierId)
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Step("Create some order on metro station")
    public int createOrderAndGetTrackId(int uniqueNumber, String metroStation) {
        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(
                "Naruto",
                "Uzumaki",
                "Kanoha, 142 apt.",
                metroStation,
                "+7 800 355 35 35",
                uniqueNumber,
                "2023-03-22",
                "order " + uniqueNumber,
                List.of("BLACK")
        );

        return client.orderCreate(requestDto)
                .statusCode(201)
                .extract()
                .as(OrderCreateResponseDto.class)
                .getTrack();
    }

    @Step("Create some order and assign it to courier")
    public int createOrderAndAssignToCourierAndGetTrackId(int uniqueNumber, String metroStation, int courierId) {
        int trackId = createOrderAndGetTrackId(uniqueNumber, metroStation);
        OrderGetOrderDto orderDto = getOrderByTrackId(trackId);
        acceptOrderToCourier(orderDto.getId(), courierId);
        return trackId;
    }

    @Step("List orders")
    public OrdersListResponseDto listOrders(Integer courierId, String nearestStation, Integer limit, Integer page) {
        return client.listOrders(courierId, nearestStation, limit, page)
                .statusCode(200)
                .extract()
                .as(OrdersListResponseDto.class);
    }
}
