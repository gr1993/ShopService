package park.shop.domain.member;

public enum GenderType {
    M("남자"), F("여자");

    private final String description;

    GenderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
