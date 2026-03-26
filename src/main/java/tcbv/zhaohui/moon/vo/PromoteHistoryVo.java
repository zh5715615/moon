package tcbv.zhaohui.moon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: zhaohui
 * @Title: PromoteHistoryVo
 * @Description:
 * @date: 2026/1/1 17:09
 */
@Data
@ApiModel("推广历史记录响应实体")
public class PromoteHistoryVo {
    @ApiModelProperty("质押hash值")
    private String hash;

    @ApiModelProperty("质押人地址")
    private String address;

    @ApiModelProperty("质押时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pledgeTime;

    @ApiModelProperty("质押金额")
    private Double pledgeAmount;
}
