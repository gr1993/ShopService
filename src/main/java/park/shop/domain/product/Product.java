package park.shop.domain.product;

import lombok.Data;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;
import park.shop.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @NotNull @Column(length = 250)
    private String name;

    @Column
    private Integer price;

    @Column(name = "sale_price")
    private Integer salePrice;

    @Column
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="main_image_id")
    private File mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="desc_image_group_id")
    private FileGroup descImageGroup;

    @Column
    private LocalDateTime createDt;

    @Column
    private LocalDateTime updateDt;

    public Product() {
    }

    @PrePersist
    public void prePersist() {
        this.createDt = LocalDateTime.now();
        this.updateDt = this.createDt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDt = LocalDateTime.now();
    }
}
