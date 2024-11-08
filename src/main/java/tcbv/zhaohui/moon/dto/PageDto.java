package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页请求实体")
public class PageDto {
    @ApiModelProperty(value = "页码")
    protected int pageNum;

    @ApiModelProperty(value = "页大小")
    protected int pageSize;
}
