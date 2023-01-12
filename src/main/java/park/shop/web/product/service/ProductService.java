package park.shop.web.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import park.shop.common.dto.Pageable;
import park.shop.common.util.FileUtil;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;
import park.shop.domain.member.Member;
import park.shop.domain.product.Product;
import park.shop.repository.file.FileRepository;
import park.shop.repository.member.MemberRepository;
import park.shop.repository.product.ProductRepository;
import park.shop.repository.product.ProductSearchCond;
import park.shop.repository.product.ProductUpdateDto;
import park.shop.web.product.dto.*;
import park.shop.web.util.formatter.LocalDateFormatter;
import park.shop.web.util.formatter.LocalDateTimeFormatter;
import park.shop.web.util.formatter.NumberFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
            productInfoDto.setId(product.getId());
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

    public Long findAllCount(ProductSearchCond cond) {
        return productRepository.findAllCount(cond);
    }

    public ProductDescDto findById(Long id) {
        LocalDateFormatter localDateFormatter = new LocalDateFormatter();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return null;
        }

        ProductDescDto result = new ProductDescDto();
        result.setId(product.getId());
        result.setName(product.getName());
        result.setPrice(product.getPrice());
        result.setSalePrice(product.getSalePrice());
        result.setQuantity(product.getQuantity());
        result.setCreateDt(localDateFormatter.print(product.getCreateDt(), Locale.KOREA));

        if(product.getMainImage() != null) {
            File mainImage = product.getMainImage();
            result.setMainImageId(mainImage.getId());
            result.setMainImageName(mainImage.getName());
        }

        if(product.getDescImageGroup() != null) {
            List<File> descImages = product.getDescImageGroup().getFiles();
            List<Long> descImageIds = descImages
                    .stream()
                    .map(desc -> desc.getId())
                    .collect(Collectors.toList());

            result.setDescImageIds(descImageIds.toArray(new Long[descImageIds.size()]));

            List<String> descImageNames = descImages
                    .stream()
                    .map(desc -> desc.getName())
                    .collect(Collectors.toList());

            result.setDescImageNames(String.join(", ", descImageNames));
        }

        return result;
    }

    public ProductDescFormDto findByIdAboutForm(Long id) {
        NumberFormatter numberFormatter = new NumberFormatter();
        ProductDescDto productDescDto = findById(id);

        ProductDescFormDto result = new ProductDescFormDto();
        result.setId(productDescDto.getId());
        result.setName(productDescDto.getName());
        if (productDescDto.getPrice() != null) {
            result.setPrice(numberFormatter.print(productDescDto.getPrice(), Locale.KOREA) + "원");
        }
        if (productDescDto.getSalePrice() != null) {
            result.setSalePrice(numberFormatter.print(productDescDto.getSalePrice(), Locale.KOREA) + "원'");
        }
        result.setQuantity(productDescDto.getQuantity());
        result.setCreateDt(productDescDto.getCreateDt());
        result.setMainImageId(productDescDto.getMainImageId());
        result.setDescImageIds(productDescDto.getDescImageIds());
        return result;
    }

    public void updateProduct(Long id, ProductUpdateFormDto updateFormDto) {
        File mainImage = null;
        if (updateFormDto.getMainImage() != null) {
            MultipartFile file = updateFormDto.getMainImage();
            String uploadedPath = fileUtil.uploadFile(file);
            mainImage = new File();
            mainImage.setName(file.getOriginalFilename());
            mainImage.setPath(uploadedPath);
            fileRepository.save(mainImage);
        }

        FileGroup fileGroup = null;
        if (updateFormDto.getDescImageGroup() != null) {
            fileGroup = new FileGroup();
            fileRepository.save(fileGroup);

            for(MultipartFile file : updateFormDto.getDescImageGroup()) {
                String uploadedPath = fileUtil.uploadFile(file);
                File descImage1 = new File();
                descImage1.setName(file.getOriginalFilename());
                descImage1.setPath(uploadedPath);
                descImage1.setFileGroup(fileGroup);
                fileRepository.save(descImage1);
            }
        }

        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setName(updateFormDto.getName());
        updateDto.setPrice(updateFormDto.getPrice());
        updateDto.setSalePrice(updateFormDto.getSalePrice());
        updateDto.setQuantity(updateFormDto.getQuantity());
        if (mainImage != null) {
            updateDto.setMainImage(mainImage);
        }
        if (fileGroup != null) {
            updateDto.setDescImageGroup(fileGroup);
        }
        productRepository.update(id, updateDto);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
