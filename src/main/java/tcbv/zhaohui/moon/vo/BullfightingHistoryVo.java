package tcbv.zhaohui.moon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: zhaohui
 * @Title: BullfightingHistoryVo
 * @Description:
 * @date: 2026/1/14 9:08
 */
@Data
@ApiModel("斗牛历史记录")
public class BullfightingHistoryVo {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("胜负（true胜，false负）")
    private boolean winlose;

    @ApiModelProperty("得分")
    private int score;

    @ApiModelProperty("时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
}
