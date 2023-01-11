package park.shop.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pageable {
    private Integer pageSize;
    private Integer page;
    private Integer offset;

    public Pageable(Integer pageSize, Integer page) {
        this.pageSize = pageSize;
        this.page = page;
    }

    public Integer getOffset() {
        if (page == null) {
            return 0;
        }
        return (pageSize * page) - pageSize;
    }
}
