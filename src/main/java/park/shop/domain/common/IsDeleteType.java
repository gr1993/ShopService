package park.shop.domain.common;

public enum IsDeleteType {
    Y("삭제"), N("삭제 안함");

    private String description;

    IsDeleteType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
