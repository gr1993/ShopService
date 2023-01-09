package park.shop.domain.member;

public enum LoginType {
    WEB("일반"), KAKAO("카카오");

    private String description;

    LoginType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
