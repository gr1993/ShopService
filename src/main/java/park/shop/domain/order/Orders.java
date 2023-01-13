package park.shop.domain.order;

import lombok.Data;
import park.shop.domain.member.Member;
import park.shop.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;

    @Column
    private Integer price;

    @Column
    private Integer quantity;

    @Column(name = "is_payment", length = 5)
    private String isPayment;

    @Column(length = 50)
    private String pg;

    @Column(name = "merchant_uid", length = 100)
    private String merchantUid;

    @Column
    private LocalDateTime createDt;

    @Column
    private LocalDateTime updateDt;

    public Orders() {
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
