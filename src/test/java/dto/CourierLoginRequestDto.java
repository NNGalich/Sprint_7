package dto;

public class CourierLoginRequestDto {
    private String login;
    private String password;

    public CourierLoginRequestDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierLoginRequestDto() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
