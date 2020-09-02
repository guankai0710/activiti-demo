package com.guankai.activitidemo.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * ACT_FROM_CONTENT
 * @author 
 */
public class ActFromContent implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 流程实例id
     */
    private String procInstId;

    /**
     * 任务节点id
     */
    private String taskId;

    /**
     * 表单key
     */
    private String fromKey;

    /**
     * 表单内容
     */
    private String content;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFromKey() {
        return fromKey;
    }

    public void setFromKey(String fromKey) {
        this.fromKey = fromKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}