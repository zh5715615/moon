package tcbv.zhaohui.moon.oss;

import lombok.Builder;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: UploadResult
 * @Description:
 * @date: 2025/12/2 21:03
 */
@Data
@Builder
public class UploadResult {

    private String filename;

    private String url;

}