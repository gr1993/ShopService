package park.shop.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import park.shop.domain.member.Member;
import park.shop.web.argumentresolver.Login;
import park.shop.web.member.dto.MemberLoginDto;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(@Login Member member, Model model) {
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new MemberLoginDto());
        return "login";
    }
}
