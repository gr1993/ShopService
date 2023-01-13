package park.shop.repository.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import park.shop.common.util.EncryptUtil;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;
import park.shop.domain.member.GenderType;
import park.shop.domain.member.Member;
import park.shop.domain.order.Orders;
import park.shop.domain.product.Product;
import park.shop.repository.file.FileRepository;
import park.shop.repository.member.MemberRepository;
import park.shop.repository.product.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    FileRepository fileRepository;

    @Test
    void save() {
        //given
        Member member = createDumpMember("1", GenderType.M);
        Member savedMember = memberRepository.save(member);

        ProductResult result1 = createProductDate();
        productRepository.save(result1.product);
        Product savedProduct = result1.product;

        Orders orders = new Orders();
        orders.setMember(savedMember);
        orders.setProduct(savedProduct);
        orders.setPrice(savedProduct.getPrice());
        orders.setQuantity(savedProduct.getQuantity());
        orders.setIsPayment("Y");
        orders.setPg("KAKAO");
        orders.setMerchantUid("ORD20230101-0000001");

        //when
        Orders savedOrder = orderRepository.save(orders);

        //then
        assertThat(orders).isEqualTo(savedOrder);
    }

    @Test
    void getMaxMerchantUid() {
        String uid = orderRepository.findMaxByMerchantUidLike("ORD20230113");
        System.out.println("uid = " + uid);
    }

    Member createDumpMember(String addText, GenderType gender) {
        Member member = new Member();
        member.setLoginId("test" + addText);
        String pwd = "pw" + addText;
        String salt = EncryptUtil.getSalt();
        String encryptedPwd = EncryptUtil.getEncrypt(pwd, salt);
        member.setPassword(encryptedPwd);
        member.setSalt(salt);
        member.setName("박강림" + addText);
        member.setRole("일반" + addText);
        member.setGender(gender.toString());
        member.setLoginType("web" + addText);
        member.setAddress("주소" + addText);

        return member;
    }

    private ProductResult createProductDate() {
        Member member = createDumpMember("1", GenderType.M);
        memberRepository.save(member);

        File mainImage = new File();
        mainImage.setName("이미지.jpg");
        mainImage.setPath("/img/image.jpg");
        fileRepository.save(mainImage);

        FileGroup fileGroup = new FileGroup();
        fileRepository.save(fileGroup);
        File descImage1 = new File();
        descImage1.setName("설명1.jpg");
        descImage1.setPath("/img/desc1.jpg");
        descImage1.setFileGroup(fileGroup);
        File descImage2 = new File();
        descImage2.setName("설명2.jpg");
        descImage2.setPath("/img/desc2.jpg");
        descImage2.setFileGroup(fileGroup);
        fileRepository.save(descImage1);
        fileRepository.save(descImage2);

        Product product = new Product();
        product.setMember(member);
        product.setName("신상품");
        product.setPrice(3000);
        product.setSalePrice(2500);
        product.setQuantity(10);
        product.setMainImage(mainImage);
        product.setDescImageGroup(fileGroup);

        return new ProductResult(member, product);
    }

    public class ProductResult {
        Member member;
        Product product;

        public ProductResult(Member member, Product product) {
            this.member = member;
            this.product = product;
        }
    }
}