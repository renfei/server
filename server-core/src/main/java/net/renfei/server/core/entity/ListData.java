package net.renfei.server.core.entity;

import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页查询响应对象
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "分页查询响应对象")
public class ListData<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "当前页码")
    protected int pageNum = 1;
    @Schema(description = "每页容量")
    protected int pageSize = 10;
    @Schema(description = "起始行")
    protected long startRow = 0;
    @Schema(description = "结束行")
    protected long endRow = 0;
    @Schema(description = "总数量")
    protected long total = 0;
    @Schema(description = "总页数")
    protected int pages = 0;
    @Schema(description = "数据负载")
    protected List<T> data;

    public ListData(Page<T> page) {
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.startRow = page.getStartRow();
        this.endRow = page.getEndRow();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.data = page.getResult();
    }

    public ListData(Page<?> page, List<T> data) {
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.startRow = page.getStartRow();
        this.endRow = page.getEndRow();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.data = data;
    }
}
