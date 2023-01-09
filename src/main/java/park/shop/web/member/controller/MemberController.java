package park.shop.web.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String register(@ModelAttribute MemberRegisterDto memberRegisterDto) {
        memberService.registerMember(memberRegisterDto);
        return "redirect:/login";
    }
}
