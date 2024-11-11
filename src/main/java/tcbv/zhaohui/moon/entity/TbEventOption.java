package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbEventOption)实体类
 *
 * @author makejava
 * @since 2024-11-11 16:34:02
 */
public class TbEventOption implements Serializable {
    private static final long serialVersionUID = -18393601082310207L;
/**
     * 主键id
     */
    private String id;
/**
     * 事件id
     */
    private String eventId;
/**
     * 选项
     */
    private String option;
/**
     * 创建时间
     */
    private Date createTime;
/**
     * 更新时间
     */
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}

