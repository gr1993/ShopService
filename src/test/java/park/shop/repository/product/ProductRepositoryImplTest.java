package park.shop.repository.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import park.shop.common.dto.Pageable;
import park.shop.common.util.EncryptUtil;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;
import park.shop.domain.member.GenderType;
import park.shop.domain.member.Member;
import park.shop.domain.product.Product;
import park.shop.repository.file.FileRepository;
import park.shop.repository.member.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
class ProductRepositoryImplTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FileRepository fileRepository;

    @Test
    void save() {
        //given
        ProductResult result = createProductDate();

        //when
        Product savedProduct = productRepository.save(result.product);

        //then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct).isEqualTo(result.product);
    }

    @Test
    void findAll() {
        //given
        ProductResult result1 = createProductDate();
        productRepository.save(result1.product);
        ProductResult result2 = createProductDate();
        productRepository.save(result2.product);

        //when
        ProductSearchCond cond = new ProductSearchCond();
        cond.setName(result1.product.getName());
        Pageable pageable = new Pageable(1, 1);
        List<Product> productList = productRepository.findAll(cond, pageable);
        Long count = productRepository.findAllCount(cond);

        //then
        assertThat(productList.size()).isEqualTo(1);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void remove() {
        //given
        ProductResult result1 = createProductDate();
        productRepository.save(result1.product);

        //when
        productRepository.deleteById(result1.product.getId());
    }

    Member createDumpMember(String addText, GenderType gender) {
        Member member = new Member();
        member.setLoginId("test" + addText);
        String pwd = "pw" + addText;
        String salt = EncryptUtil.getSalt();
        String encryptedPwd = EncryptUtil.getEncrypt(pwd, salt);
        member.setPassword(encryptedPwd);
        member.setSalt(salt);
        member.setName("?????????" + addText);
        member.setRole("??????" + addText);
        member.setGender(gender.toString());
        member.setLoginType("web" + addText);
        member.setAddress("??????" + addText);

        return member;
    }

    private ProductResult createProductDate() {
        Member member = createDumpMember("1", GenderType.M);
        memberRepository.save(member);

        File mainImage = new File();
        mainImage.setName("?????????.jpg");
        mainImage.setPath("/img/image.jpg");
        fileRepository.save(mainImage);

        FileGroup fileGroup = new FileGroup();
        fileRepository.save(fileGroup);
        File descImage1 = new File();
        descImage1.setName("??????1.jpg");
        descImage1.setPath("/img/desc1.jpg");
        descImage1.setFileGroup(fileGroup);
        File descImage2 = new File();
        descImage2.setName("??????2.jpg");
        descImage2.setPath("/img/desc2.jpg");
        descImage2.setFileGroup(fileGroup);
        fileRepository.save(descImage1);
        fileRepository.save(descImage2);

        Product product = new Product();
        product.setMember(member);
        product.setName("?????????");
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