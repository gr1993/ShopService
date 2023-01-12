package park.shop.web.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import park.shop.common.dto.ResultDto;
import park.shop.domain.member.Member;
import park.shop.web.order.dto.OrderInfoDto;
import park.shop.web.order.service.OrderService;
import park.shop.web.util.argumentresolver.Login;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @PostMapping("/info")
    public Object orderProductInfo(
            @Login Member member,
            @RequestParam Long productId,
            @RequestParam Integer orderQuantity
    ) {
        OrderInfoDto orderInfo = orderService.getOrderInfo(member.getId(), productId, orderQuantity);

        ResultDto resultDto = new ResultDto(true);
        resultDto.setData(orderInfo);
        return resultDto;
    }
}