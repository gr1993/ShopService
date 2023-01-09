package park.shop.repository.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchCond {
    private String loginId;
    private String name;

    public MemberSearchCond(String loginId, String name) {
        this.loginId = loginId;
        this.name = name;
    }
}
