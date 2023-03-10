package park.shop.web.member.dto;

import lombok.Getter;
import lombok.Setter;
import park.shop.domain.member.GenderType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class MemberRegisterDto {

    @NotBlank
    @Size(min = 4, max = 20)
    private String loginId;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    @NotBlank
    @Size(min = 6, max = 20)
    private String passwordCheck;

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    @NotBlank
    @Size(min = 2, max = 250)
    private String address;

    private GenderType gender;
}
