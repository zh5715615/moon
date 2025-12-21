package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: PledgeDto
 * @Description:
 * @date: 2025/12/21 11:01
 */
@Data
@ApiModel("质押信息")
public class PledgeDto {
    @ApiModelProperty("质押交易hash值")
    private String hash;
}
