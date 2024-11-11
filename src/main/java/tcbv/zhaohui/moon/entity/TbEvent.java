package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbEvent)实体类
 *
 * @author makejava
 * @since 2024-11-11 17:06:51
 */
public class TbEvent implements Serializable {
    private static final long serialVersionUID = -44003241752990490L;
/**
     * 事件id
     */
    private Integer id;
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
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

    public Date getBetTime() {
        return betTime;
    }

    public void setBetTime(Date betTime) {
        this.betTime = betTime;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

