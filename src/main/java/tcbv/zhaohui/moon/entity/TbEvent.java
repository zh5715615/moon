package tcbv.zhaohui.moon.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbEvent)实体类
 *
 * @author makejava
 * @since 2024-11-11 17:06:51
 */
@Data
public class TbEvent implements Serializable {
    private static final long serialVersionUID = -44003241752990490L;
/**
     * 事件id
     */
    private String id;
/**
     * 事件名称
     */
    private String name;
/**
     * 事件内容
     */
    private String content;
/**
     * 事件结果
     */
    private String optionId;
/**
     * 事件创建时间
     */
    private Date createTime;
/**
     * 更新内容时间
     */
    private Date updateTime;
/**
     * 开始投注时间
     */
    private Date betTime;
/**
     * 事件结果时间(开奖时间)
     */
    private Date resultTime;
/**
     * 计算状态;1有效 | 2注销 | 3已结算
     */
    private Integer status;
}

