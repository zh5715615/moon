package tcbv.zhaohui.moon.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ChainTxTaskEntity {
    private String id;
    private String txHash;
    private String bizType;
    private String bizParams;
    /** 0=PENDING 1=PROCESSING 2=SUCCESS 3=FAILED */
    private Integer status;
    private Integer retryCount;
    private String errorMsg;
    private Date createTime;
    private Date updateTime;
}
