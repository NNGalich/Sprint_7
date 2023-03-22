package tests;

import java.util.List;

import client.ScootersTestClient;
import constants.ServerUrl;
import dto.OrderCreateRequestDto;
import dto.OrderCreateResponseDto;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<String> orderColor;

    private ScootersTestClient client;

    public CreateOrderTest(List<String> orderColor) {
        this.orderColor = orderColor;
    }

    @Parameterized.Parameters
    public static Object[][] availableColors() {
        return new Object[][]{
                { List.of("BLACK") },
                { List.of("GREY") },
                { List.of("BLACK", "GREY") },
                { List.of() }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new ScootersTestClient();
    }

    @Test
    public void createOrderWithColorTest() {
        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(
                "Senior",
                "Pomidor",
                "Tisovaya ulitsa, 12",
                "3",
                "+7 800 555 35 35",
                5,
                "2023-03-22",
                "Nothing special",
                orderColor
        );
        OrderCreateResponseDto createOrderResponseDto = client.orderCreate(requestDto)
                .statusCode(201)
                .extract()
                .as(OrderCreateResponseDto.class);

        // Тут постоянно падает и возвращает не тот цвет, который был создан
        // Тест при этом похож на правильный, похоже на баг на самом сайте
        client.orderGet(createOrderResponseDto.getTrack())
                .statusCode(200)
                .body("order.color", equalTo(orderColor));
    }
}
