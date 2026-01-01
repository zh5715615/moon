package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: PromoteInfoVo
 * @Description:
 * @date: 2026/1/1 16:34
 */
@Data
@ApiModel("推广奖励信息响应体")
public class PromoteInfoVo {
    @ApiModelProperty("总奖励")
    private long total;

    @ApiModelProperty("剩余奖励数量")
    private double free;
}
