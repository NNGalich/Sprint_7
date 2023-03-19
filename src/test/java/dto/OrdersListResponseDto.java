package dto;

import java.util.List;

public class OrdersListResponseDto {
    private List<OrderListOrderDto> orders;
    private PageInfoDto pageInfo;
    private List<AvailableStationDto> availableStations;

    public OrdersListResponseDto() {
    }

    public List<OrderListOrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderListOrderDto> orders) {
        this.orders = orders;
    }

    public PageInfoDto getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoDto pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStationDto> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStationDto> availableStations) {
        this.availableStations = availableStations;
    }
}
