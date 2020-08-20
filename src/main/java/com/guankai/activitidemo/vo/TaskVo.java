package com.guankai.activitidemo.vo;

/**
 * 类描述：TODO
 *
 * @author guankai
 * @date 2020/8/20
 **/
public class TaskVo {

    /** 任务id */
    private String id;

    /** 任务名称 */
    private String name;

    /** 任务处理人 */
    private String assignee;

    /** 任务key（对应流程图中节点的id） */
    private String taskDefinitionKey;

    /** 流程实例id */
    private String processInstanceId;

    /** 流程业务key */
    private String businessKey;

    /** 节点表单key */
    private String formKey;

    public TaskVo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }
}
