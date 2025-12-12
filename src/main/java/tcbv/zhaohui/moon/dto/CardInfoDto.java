package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author: zhaohui
 * @Title: CardInfoDto
 * @Description:
 * @date: 2025/12/12 13:26
 */
@Data
@ApiModel("卡片信息")
public class CardInfoDto {
    @ApiModelProperty("卡片名称")
    @NotBlank(message = "卡片名称不能为空")
    private String name;

    @ApiModelProperty("卡片描述")
    private String description;

    @ApiModelProperty("卡片图片")
    @NotNull(message = "卡片图片不能为空")
    private MultipartFile file;

    @ApiModelProperty("卡片属性")
    private Map<String, String> attributes;
}
