//package com.guankai.activitidemo.model;
//
//import org.activiti.bpmn.model.SequenceFlow;
//
//import java.util.List;
//
///**
// * 类描述：自定义封装节点信息
// *
// * @author guankai
// * @date 2020/8/17
// **/
//public class LeaveTask {
//
//    /** 流程定义id */
//    private String processDefinitionId;
//
//    /** 流程实例id */
//    private String processInstanceId;
//
//    /** 节点id */
//    private String taskId;
//
//    /** 节点key（对应流程图中节点的id） */
//    private String taskDefinitionKey;
//
//    /** 节点名称 */
//    private String taskName;
//
//    /** 节点表单key */
//    private String formKey;
//
//    /** 节点处理人 */
//    private String assignee;
//
//    /** 节点类型 */
//    private Class taskType;
//
//    /** 进入当前节点流信息 */
//    private List<SequenceFlow> incomingFlows;
//
//    /** 进入下一节点流信息 */
//    private List<SequenceFlow> outgoingFlows;
//
//    public LeaveTask() {
//    }
//
//    public String getProcessDefinitionId() {
//        return processDefinitionId;
//    }
//
//    public void setProcessDefinitionId(String processDefinitionId) {
//        this.processDefinitionId = processDefinitionId;
//    }
//
//    public String getProcessInstanceId() {
//        return processInstanceId;
//    }
//
//    public void setProcessInstanceId(String processInstanceId) {
//        this.processInstanceId = processInstanceId;
//    }
//
//    public String getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }
//
//    public String getTaskDefinitionKey() {
//        return taskDefinitionKey;
//    }
//
//    public void setTaskDefinitionKey(String taskDefinitionKey) {
//        this.taskDefinitionKey = taskDefinitionKey;
//    }
//
//    public String getTaskName() {
//        return taskName;
//    }
//
//    public void setTaskName(String taskName) {
//        this.taskName = taskName;
//    }
//
//    public String getFormKey() {
//        return formKey;
//    }
//
//    public void setFormKey(String formKey) {
//        this.formKey = formKey;
//    }
//
//    public String getAssignee() {
//        return assignee;
//    }
//
//    public void setAssignee(String assignee) {
//        this.assignee = assignee;
//    }
//
//    public Class getTaskType() {
//        return taskType;
//    }
//
//    public void setTaskType(Class taskType) {
//        this.taskType = taskType;
//    }
//
//    public List<SequenceFlow> getIncomingFlows() {
//        return incomingFlows;
//    }
//
//    public void setIncomingFlows(List<SequenceFlow> incomingFlows) {
//        this.incomingFlows = incomingFlows;
//    }
//
//    public List<SequenceFlow> getOutgoingFlows() {
//        return outgoingFlows;
//    }
//
//    public void setOutgoingFlows(List<SequenceFlow> outgoingFlows) {
//        this.outgoingFlows = outgoingFlows;
//    }
//}
