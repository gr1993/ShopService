package park.shop.web.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import park.shop.domain.member.GenderType;
import park.shop.web.member.dto.MemberRegisterDto;
import park.shop.web.member.service.MemberService;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ModelAttribute("genderTypes")
    public GenderType[] genderTypes() {
        return GenderType.values();
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", new MemberRegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Validated @ModelAttribute("member") MemberRegisterDto memberRegisterDto,
            BindingResult bindingResult,
            Model model) {

        if (memberRegisterDto.getGender() == null) {
            bindingResult.addError(new FieldError("member", "gender", null, false, new String[] {"NotBlank.member.gender"}, null, null));
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "register";
        }

        memberService.registerMember(memberRegisterDto);
        model.addAttribute("isSuccess", true);
        return "register";
    }
}
