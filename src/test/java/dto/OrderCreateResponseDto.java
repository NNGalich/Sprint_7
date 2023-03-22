package dto;

public class OrderCreateResponseDto {
    private int track;

    public OrderCreateResponseDto(int track) {
        this.track = track;
    }

    public OrderCreateResponseDto() {
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }
}
