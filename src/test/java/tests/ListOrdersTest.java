package tests;

import java.util.List;

import client.ScootersTestClient;
import constants.Couriers;
import constants.ServerUrl;
import dto.OrderListOrderDto;
import dto.OrdersListResponseDto;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.ScooterTestSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ListOrdersTest {

    private static final String METRO_STATION = "5";

    private ScootersTestClient client;
    private ScooterTestSteps steps;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new ScootersTestClient();
        steps = new ScooterTestSteps(client);
    }

    @Test
    @Description("ListOrders without filters returns some orders")
    public void listOrdersWithoutFiltersWorks() {
        OrdersListResponseDto responseDto = client.listOrders(null, null, null, null)
                .statusCode(200)
                .extract()
                .as(OrdersListResponseDto.class);

        assertThat("Expected 30 elements in order", responseDto.getOrders().size(), equalTo(30));
        assertThat("Expected page to be a zero", responseDto.getPageInfo().getPage(), equalTo(0));
        assertThat("Expected limit to be 30", responseDto.getPageInfo().getLimit(), equalTo(30));
    }

    @Test
    @Description("ListOrders with courier returns orders for current courier only")
    public void listOrdersWithCourierFilterReturnsOnlyCurrentCourier() {
        steps.createCourier(Couriers.LOGIN, Couriers.PASSWORD, Couriers.FIRST_NAME);
        int courierId = steps.loginCourierAndGetId(Couriers.LOGIN, Couriers.PASSWORD);

        steps.createOrderAndAssignToCourierAndGetTrackId(0, "1", courierId);
        steps.createOrderAndAssignToCourierAndGetTrackId(2, "1", courierId);

        OrdersListResponseDto responseDto = client.listOrders(courierId, null, null, null)
                .statusCode(200)
                .extract()
                .as(OrdersListResponseDto.class);

        for (OrderListOrderDto orderDto : responseDto.getOrders()) {
            assertThat("All orders must be assigned to the courier", orderDto.getCourierId(), equalTo(courierId));
        }
    }

    @Test
    @Description("ListOrders with limit 2 returns 2 orders only")
    public void listOrdersWithLimit2ReturnsOnly2Orders() {
        client.listOrders(null, null, 2, null)
                .statusCode(200)
                .body("orders", hasSize(2))
                .body("pageInfo.limit", equalTo(2));
    }

    @Test
    @Description("ListOrders for page 2 returns second page")
    public void listOrdersWithDifferentPageParamReturnsDifferentOrders() {
        OrdersListResponseDto firstPageResponse = steps.listOrders(null, null, 2, 0);
        assertThat("Page mismatch", firstPageResponse.getPageInfo().getPage(), equalTo(0));
        List<Integer> firstPageOrderIds = List.of(
                firstPageResponse.getOrders().get(0).getId(),
                firstPageResponse.getOrders().get(1).getId()
        );

        OrdersListResponseDto secondPageResponse = steps.listOrders(null, null, 2, 1);
        assertThat("Page mismatch", secondPageResponse.getPageInfo().getPage(), equalTo(1));
        List<Integer> secondPageOrderIds = List.of(
                secondPageResponse.getOrders().get(0).getId(),
                secondPageResponse.getOrders().get(1).getId()
        );

        assertThat("Both pages has the same orders", secondPageOrderIds, not(equalTo(firstPageOrderIds)));
    }

    @Test
    @Description("ListOrders with metro station filter return orders for given metro station only")
    public void listOrdersWithMetroStationReturnsOrdersForGivenMetroStationOnly() {
        steps.createOrderAndGetTrackId(0, METRO_STATION);
        steps.createOrderAndGetTrackId(1, METRO_STATION);

        OrdersListResponseDto responseDto = steps.listOrders(null, METRO_STATION, null, null);
        for (OrderListOrderDto orderDto : responseDto.getOrders()) {
            assertThat(orderDto.getMetroStation(), equalTo(METRO_STATION));
        }
    }

    @After
    public void tearDown() throws Exception {
        steps.deleteCourierIfExists(Couriers.LOGIN, Couriers.PASSWORD);
    }
}
