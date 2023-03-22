package dto;

public class OrderGetResponseDto {
    private OrderGetOrderDto order;

    public OrderGetResponseDto(OrderGetOrderDto order) {
        this.order = order;
    }

    public OrderGetResponseDto() {
    }

    public OrderGetOrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderGetOrderDto order) {
        this.order = order;
    }
}
