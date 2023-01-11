package park.shop.domain.file;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="file_group_id")
    private FileGroup fileGroup;

    @Column(length = 250)
    private String name;

    @Column(length = 1000)
    private String path;

    @Column
    private LocalDateTime createDt;

    @Column
    private LocalDateTime updateDt;

    public File() {
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
