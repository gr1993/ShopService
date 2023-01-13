package park.shop.web.order.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Test
    void getToken() throws IOException {
        String token = paymentService.getToken();
        System.out.println("token = " + token);
    }

//    @Test
//    void cancel() throws IOException {
//        String token = paymentService.getToken();
//
//        paymentService.paymentCancel(token, "ORD20230113_0000005", 2500, "사용자 취소");
//    }
}