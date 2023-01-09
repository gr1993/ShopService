package park.shop.domain.member;

public enum RoleType {
    NORMAL("일반"), SELLER("판매자"), ADMIN("관리자");

    private String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
