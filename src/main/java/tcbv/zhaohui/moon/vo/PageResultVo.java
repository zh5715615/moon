package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("分页返回数据")
public class PageResultVo<T> {
    @ApiModelProperty(name = "查询总数")
    private long total;

    @ApiModelProperty(name = "查询结果列表")
    private List<T> list;

    @ApiModelProperty(name = "页码")
    private int pageNum;

    @ApiModelProperty(name = "页大小")
    private int pageSize;
}