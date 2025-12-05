package tcbv.zhaohui.moon.oss;

import lombok.Data;
import java.util.Date;

/**
 * @author: zhaohui
 * @Title: SysOss
 * @Description:
 * @date: 2025/12/2 21:05
 */
@Data
public class SysOss {

    private String id;

    private String filePrefix;

    private String fileSuffix;

    private String url;

    private String bucket;

    private String fileName;

    private String originalName;

    private String service;

    private String bizType;

    private String bizId;

    private String contentType;

    private Date createTime;

    private Date updateTime;

}