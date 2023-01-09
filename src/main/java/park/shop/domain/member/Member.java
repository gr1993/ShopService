package park.shop.domain.member;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Column(name = "login_id", length = 20)
    private String loginId;

    @NotNull @Column(length = 250)
    private String password;

    @Column(name = "login_type", length = 20)
    private String loginType;

    @Column(length = 20)
    private String name;

    @Column(length = 5)
    private String gender;

    @Column(length = 250)
    private String address;

    @Column(length = 20)
    private String role;

    @Column(name = "is_delete", length = 5)
    private String isDelete;

    @Column
    private LocalDateTime createDt;

    @Column
    private LocalDateTime updateDt;

    public Member() {
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
