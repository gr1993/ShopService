package park.shop.repository.member;

import lombok.Getter;
import lombok.Setter;
import park.shop.domain.member.GenderType;

@Setter
@Getter
public class MemberUpdateDto {
    private String password;

    private String salt;

    private String name;

    private GenderType gender;

    private String address;

    private String role;

    private String isDelete;
}
