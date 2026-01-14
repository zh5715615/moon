package tcbv.zhaohui.moon.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: zhaohui
 * @Title: RestPage
 * @Description:
 * @date: 2025/11/11 21:28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@ApiModel("分页结果")
public class RestPage<T> {
    @ApiModelProperty("当前页数据")
    private List<T> content;

    @ApiModelProperty("总记录数")
    private long totalElements;

    @ApiModelProperty("总页数")
    private int totalPages;

    @ApiModelProperty("当前页码（从0开始 or 从1开始？建议明确）")
    private int number;

    @ApiModelProperty("每页大小")
    private int size;

    @ApiModelProperty("是否第一页")
    private boolean first;

    @ApiModelProperty("是否最后一页")
    private boolean last;

    public static <T> RestPage<T> of(Page<T> page) {
        RestPage<T> restPage = new RestPage<>();
        restPage.content = page.getContent();
        restPage.totalElements = page.getTotalElements();
        restPage.totalPages = page.getTotalPages();
        restPage.number = page.getNumber();
        restPage.size = page.getSize();
        restPage.first = page.isFirst();
        restPage.last = page.isLast();
        return restPage;
    }

    public static RestPage empty() {
        return RestPage.of(Page.empty());
    }
}
