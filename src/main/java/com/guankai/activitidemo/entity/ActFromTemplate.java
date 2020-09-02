package com.guankai.activitidemo.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * ACT_FROM_TEMPLATE
 * @author 
 */
public class ActFromTemplate implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 表单key
     */
    private String fromKey;

    /**
     * 模板类型，1：URL形式；2：HTML形式
     */
    private Integer tempType;

    /**
     * 表单模板
     */
    private String fromTemp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否已删除，0：否；1：是
     */
    private Integer deleted;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromKey() {
        return fromKey;
    }

    public void setFromKey(String fromKey) {
        this.fromKey = fromKey;
    }

    public Integer getTempType() {
        return tempType;
    }

    public void setTempType(Integer tempType) {
        this.tempType = tempType;
    }

    public String getFromTemp() {
        return fromTemp;
    }

    public void setFromTemp(String fromTemp) {
        this.fromTemp = fromTemp;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}