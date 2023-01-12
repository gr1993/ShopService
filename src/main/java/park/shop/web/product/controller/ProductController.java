package park.shop.web.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import park.shop.common.dto.Pageable;
import park.shop.common.dto.ResultDto;
import park.shop.domain.file.File;
import park.shop.domain.member.Member;
import park.shop.repository.file.FileRepository;
import park.shop.repository.product.ProductSearchCond;
import park.shop.web.product.dto.ProductDescDto;
import park.shop.web.product.dto.ProductInfoDto;
import park.shop.web.product.dto.ProductRegisterDto;
import park.shop.web.product.dto.ProductUpdateFormDto;
import park.shop.web.product.service.ProductService;
import park.shop.web.util.argumentresolver.Login;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MessageSource ms;

    @GetMapping("/member")
    public String productForm() {
        return "product";
    }

    @ResponseBody
    @GetMapping("/image/{fileId}")
    public Resource downloadImage(@PathVariable Long fileId) throws MalformedURLException {
        File file = fileRepository.findById(fileId).orElse(null);

        return new UrlResource("file:" + file.getPath());
    }


    @ResponseBody
    @GetMapping("/member/list")
    public Object productList(
            @Login Member member,
            @ModelAttribute ProductSearchCond productSearchCond,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        if (productSearchCond == null) {
            productSearchCond = new ProductSearchCond();
        }
        productSearchCond.setMember(member);

        List<ProductInfoDto> productList = productService.findAll(productSearchCond, new Pageable(pageSize, page));
        Long totalCount = productService.findAllCount(productSearchCond);

        ResultDto resultDto = new ResultDto(true);
        resultDto.setData(productList);
        resultDto.setTotalCount(totalCount);
        return resultDto;
    }

    @ResponseBody
    @PostMapping("/register")
    public Object productRegister(
            @Login Member member,
            @Validated @ModelAttribute ProductRegisterDto productRegisterDto,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> ms.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), Locale.KOREA))
                    .collect(Collectors.toList());

            ResultDto resultDto = new ResultDto(false);
            resultDto.setMessage(String.join(", ", errorMessages));
            return resultDto;
        }

        productService.registerProduct(productRegisterDto, member.getId());

        return new ResultDto(true);
    }

    @ResponseBody
    @GetMapping("/desc/{id}")
    public Object productInfo(@PathVariable Long id) {
        ProductDescDto productDescDto = productService.findById(id);

        ResultDto resultDto = new ResultDto(true);
        resultDto.setData(productDescDto);
        return resultDto;
    }

    @ResponseBody
    @PostMapping("/update/{id}")
    public Object productUpdate(
            @PathVariable Long id,
            @Validated @ModelAttribute ProductUpdateFormDto productUpdateFormDto,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> ms.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), Locale.KOREA))
                    .collect(Collectors.toList());

            ResultDto resultDto = new ResultDto(false);
            resultDto.setMessage(String.join(", ", errorMessages));
            return resultDto;
        }

        productService.updateProduct(id, productUpdateFormDto);

        return new ResultDto(true);
    }

    @ResponseBody
    @PostMapping("/delete/{id}")
    public Object productDelete(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);

        return new ResultDto(true);
    }
}
