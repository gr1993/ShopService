package park.shop.web.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import park.shop.common.dto.Pageable;
import park.shop.common.util.FileUtil;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;
import park.shop.domain.member.GenderType;
import park.shop.domain.member.Member;
import park.shop.domain.product.Product;
import park.shop.repository.file.FileRepository;
import park.shop.repository.member.MemberRepository;
import park.shop.repository.product.ProductRepository;
import park.shop.repository.product.ProductSearchCond;
import park.shop.web.product.dto.ProductInfoDto;
import park.shop.web.product.dto.ProductRegisterDto;
import park.shop.web.util.formatter.LocalDateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final FileRepository fileRepository;
    private final MessageSource ms;
    private final FileUtil fileUtil;

    public void registerProduct(ProductRegisterDto productRegisterDto, Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElse(null);
        if(findMember == null) {
            throw new IllegalArgumentException(ms.getMessage("NotExist.member.loginId", null, null));
        }

        File mainImage = null;
        if (productRegisterDto.getMainImage() != null) {
            MultipartFile file = productRegisterDto.getMainImage();
            String uploadedPath = fileUtil.uploadFile(file);
            mainImage = new File();
            mainImage.setName(file.getOriginalFilename());
            mainImage.setPath(uploadedPath);
            fileRepository.save(mainImage);
        }

        FileGroup fileGroup = null;
        if (productRegisterDto.getDescImageGroup() != null) {
            fileGroup = new FileGroup();
            fileRepository.save(fileGroup);

            for(MultipartFile file : productRegisterDto.getDescImageGroup()) {
                String uploadedPath = fileUtil.uploadFile(file);
                File descImage1 = new File();
                descImage1.setName(file.getOriginalFilename());
                descImage1.setPath(uploadedPath);
                descImage1.setFileGroup(fileGroup);
                fileRepository.save(descImage1);
            }
        }

        Product product = new Product();
        product.setMember(findMember);
        product.setName(productRegisterDto.getName());
        product.setPrice(productRegisterDto.getPrice());
        product.setSalePrice(productRegisterDto.getSalePrice());
        product.setQuantity(productRegisterDto.getQuantity());
        if (mainImage != null) {
            product.setMainImage(mainImage);
        }
        if (fileGroup != null) {
            product.setDescImageGroup(fileGroup);
        }
        productRepository.save(product);
    }

    public List<ProductInfoDto> findAll(ProductSearchCond cond, Pageable pageable) {
        LocalDateTimeFormatter localDateTimeFormatter = new LocalDateTimeFormatter();

        List<ProductInfoDto> result = new ArrayList<>();
        List<Product> products = productRepository.findAll(cond, pageable);
        for(Product product : products) {
            ProductInfoDto productInfoDto = new ProductInfoDto();
            if (product.getMainImage() != null) {
                productInfoDto.setMainImageId(product.getMainImage().getId());
            }
            productInfoDto.setName(product.getName());
            productInfoDto.setPrice(product.getPrice());
            productInfoDto.setSalePrice(product.getSalePrice());
            productInfoDto.setQuantity(product.getQuantity());
            productInfoDto.setCreateDt(localDateTimeFormatter.print(product.getCreateDt(), Locale.KOREA));
            productInfoDto.setUpdateDt(localDateTimeFormatter.print(product.getUpdateDt(), Locale.KOREA));

            result.add(productInfoDto);
        }

        return result;
    }
}
