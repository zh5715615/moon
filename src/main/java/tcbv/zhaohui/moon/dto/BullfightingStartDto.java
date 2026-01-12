package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author: zhaohui
 * @Title: BullfightingStartDto
 * @Description:
 * @date: 2026/1/12 21:50
 */
@Data
@ApiModel("斗牛开始入参")
public class BullfightingStartDto {
    @ApiModelProperty("押注积分")
    @Min(value = 1, message = "押注积分不能小于1")
    @Max(value = 5, message = "押注积分不能大于5")
    private int score;
}
