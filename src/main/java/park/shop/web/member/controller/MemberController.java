package park.shop.web.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import park.shop.common.SessionConst;
import park.shop.domain.member.GenderType;
import park.shop.domain.member.Member;
import park.shop.web.util.argumentresolver.Login;
import park.shop.web.member.dto.MemberInfoDto;
import park.shop.web.member.dto.MemberLoginDto;
import park.shop.web.member.dto.MemberRegisterDto;
import park.shop.web.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new MemberLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Validated @ModelAttribute("login") MemberLoginDto memberLoginDto,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "/") String redirectURL,
            HttpServletRequest request
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

        log.info("왜 앙대? : {}", redirectURL);

        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/myInfo")
    public String myInfoForm(@Login Member member, Model model) {
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setLoginId(member.getLoginId());
        memberInfoDto.setName(member.getName());
        memberInfoDto.setAddress(member.getAddress());
        memberInfoDto.setGender(GenderType.valueOf(member.getGender()));
        memberInfoDto.setCreateDt(member.getCreateDt().toString().replace('T', ' '));

        model.addAttribute("member", memberInfoDto);
        return "myInfo";
    }

    @PostMapping("/myInfo")
    public String myInfo(
            @Login Member member,
            @Validated @ModelAttribute("member") MemberInfoDto memberInfoDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (memberInfoDto.getPassword() == null && memberInfoDto.getPasswordCheck() != null) {
            bindingResult.addError(new FieldError("member", "password", null, false, new String[] {"NotBlank.member.password"}, null, null));
        }
        if (memberInfoDto.getPassword() != null && memberInfoDto.getPasswordCheck() == null) {
            bindingResult.addError(new FieldError("member", "passwordCheck", null, false, new String[] {"NotBlank.member.passwordCheck"}, null, null));
        }

        if (memberInfoDto.getPassword() != null && memberInfoDto.getPasswordCheck() != null) {
            //비밀번호 6~20자리 확인
            if (memberInfoDto.getPassword().length() < 6 || memberInfoDto.getPassword().length() > 20) {
                bindingResult.addError(new FieldError("member", "password", null, false, new String[] {"Size"}, new Object[] {0, 6, 20}, null));
            }
            //비밀번호 확인 6~20자리 확인
            if (memberInfoDto.getPasswordCheck().length() < 6 || memberInfoDto.getPasswordCheck().length() > 20) {
                bindingResult.addError(new FieldError("member", "passwordCheck", null, false, new String[] {"Size"}, new Object[] {0, 6, 20}, null));
            }
            //비밀번호와 확인이 일치하는지 확인
            if (!memberInfoDto.getPassword().equals(memberInfoDto.getPasswordCheck())) {
                bindingResult.addError(new FieldError("member", "passwordCheck", null, false, new String[] {"NotExact.member.passwordCheck"}, null, null));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "myInfo";
        }

        memberService.updateMember(member.getId(), memberInfoDto);
        model.addAttribute("isSuccess", true);
        return "myInfo";
    }

}
