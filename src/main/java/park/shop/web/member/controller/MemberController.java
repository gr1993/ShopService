package park.shop.web.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import park.shop.common.CookieConst;
import park.shop.common.SessionConst;
import park.shop.domain.member.GenderType;
import park.shop.domain.member.Member;
import park.shop.web.member.dto.MemberLoginDto;
import park.shop.web.member.dto.MemberRegisterDto;
import park.shop.web.member.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        Member findMember = memberService.findByLoginId(memberRegisterDto.getLoginId()).orElse(null);
        if (findMember != null) {
            bindingResult.addError(new FieldError("member", "loginId", null, false, new String[] {"Exist.member.loginId"}, null, null));
        }

        if (!memberRegisterDto.getPassword().equals(memberRegisterDto.getPasswordCheck())) {
            bindingResult.addError(new FieldError("member", "passwordCheck", null, false, new String[] {"NotExact.member.passwordCheck"}, null, null));
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "register";
        }

        memberService.registerMember(memberRegisterDto);
        model.addAttribute("isSuccess", true);
        return "register";
    }

    @PostMapping("/login")
    public String login(
            @Validated @ModelAttribute("login") MemberLoginDto memberLoginDto,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "/") String redirectURL,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login";
        }

        //로그인 여부 확인
        Member loginMember = memberService.login(memberLoginDto.getLoginId(), memberLoginDto.getPassword());
        if (loginMember == null) {
            bindingResult.addError(new FieldError("login", "loginId", null, false, new String[] {"NotExact.login"}, null, null));
            return "login";
        }

        //세션 생성
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
    }
}
