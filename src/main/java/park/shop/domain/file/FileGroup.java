package park.shop.domain.file;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "file_group")
public class FileGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createDt;

    @Column
    private LocalDateTime updateDt;

    public FileGroup() {
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
