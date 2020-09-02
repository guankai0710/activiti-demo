package com.guankai.activitidemo.vo;

/**
 * 类描述：流程实例信息VO
 *
 * @author guankai
 * @date 2020/8/19
 **/
public class ProcessInstanceVo {

    /** 流程实例id */
    private String id;

    /** 流程实例名称 */
    private String name;

    /** 流程定义key */
    private String processDefinitionKey;

    /** 流程定义名称 */
    private String processDefinitionName;

    /** 流程业务key */
    private String businessKey;

    /** 流程发起者 */
    private String startUserId;

    /** 流程发起时间 */
    private String startTime;

    /** 流程结束时间 */
    private String endTime;

    /** 流程是否结束 */
    private boolean finished;

    /** 开始节点表单 */
    private String startFormKey;

    public ProcessInstanceVo() {
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

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getStartFormKey() {
        return startFormKey;
    }

    public void setStartFormKey(String startFormKey) {
        this.startFormKey = startFormKey;
    }
}
