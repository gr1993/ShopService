package park.shop.repository.member;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberUpdateDto {
    private String password;

    private String name;

    private String gender;

    private String address;

    private String role;

    private String isDelete;
}
