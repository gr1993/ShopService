package park.shop.web.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class MemberLoginDto {
    @NotBlank
    @Size(min = 4, max = 20)
    private String loginId;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
