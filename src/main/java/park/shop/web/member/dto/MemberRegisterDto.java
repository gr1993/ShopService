package park.shop.web.member.dto;

import lombok.Getter;
import lombok.Setter;
import park.shop.domain.member.GenderType;

@Setter
@Getter
public class MemberRegisterDto {
    private String loginId;
    private String password;
    private String passwordCheck;

    private String name;
    private String address;
    private GenderType gender;
}
