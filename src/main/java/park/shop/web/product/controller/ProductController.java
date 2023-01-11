package park.shop.web.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import park.shop.common.dto.ResultDto;
import park.shop.domain.member.Member;
import park.shop.web.product.dto.ProductRegisterDto;
import park.shop.web.product.service.ProductService;
import park.shop.web.util.argumentresolver.Login;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MessageSource ms;

    @GetMapping("/member")
    public String productForm() {
        return "product";
    }

    @ResponseBody
    @PostMapping("/register")
    public Object register(
            @Login Member member,
            @Validated @ModelAttribute ProductRegisterDto productRegisterDto
    ) {
        log.info("getName={}", productRegisterDto.getName());
        log.info("getPrice={}", productRegisterDto.getPrice());
        log.info("getSalePrice={}", productRegisterDto.getSalePrice());
        log.info("getQuantity={}", productRegisterDto.getQuantity());
        log.info("getMainImage={}", productRegisterDto.getMainImage());
        log.info("getDescImageGroup={}", productRegisterDto.getDescImageGroup());
        if (productRegisterDto.getDescImageGroup() != null) {
            log.info("getDescImageGroupSize={}", productRegisterDto.getDescImageGroup().size());
        }

        productService.registerProduct(productRegisterDto, member.getId());

        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(true);
        return resultDto;
    }
}
