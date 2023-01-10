package park.shop.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import park.shop.domain.member.Member;
import park.shop.web.util.argumentresolver.Login;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(@Login Member member, Model model) {
        return "index";
    }
}
