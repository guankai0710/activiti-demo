package com.guankai.activitidemo.service.impl;

import com.guankai.activitidemo.model.LeaveTask;
import com.guankai.activitidemo.service.IProcessOperateService;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：流程操作接口
 *  流程启动、等
 *
 * @author guankai
 * @date 2020/8/17
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessOperateServiceImpl implements IProcessOperateService {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessOperateServiceImpl.class);


    /**
     * 根据流程key启动流程
     *
     * @param processKey 流程key
     * @author guan.kai
     * @date 2020/8/17
     * @return 流程实例信息
     **/
    @Override
    public ProcessInstance startProcessByKey(String processKey) {
        //RuntimeService：执行管理，包括启动，查询，删除流程定义
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        //启动流程，返回流程实例信息
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        LOG.info("启动流程 -> 流程key：{}，流程名称：{}，实例id：{}"
                ,processInstance.getProcessDefinitionKey(),processInstance.getProcessDefinitionName(),processInstance.getProcessInstanceId());
        return processInstance;
    }

    /**
     * 根据处理人获取待办任务列表
     *
     * @param assignee 处理人
     * @param taskService 待办任务信息接口
     * @author guankai
     * @date 2020/8/17
     * @return java.util.List<org.activiti.api.task.model.Task>
     **/
    @Override
    public List<LeaveTask> getUpComingTaskList(String assignee,TaskService taskService) {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .asc()
                .list();
        if (taskList.isEmpty()){
            return null;
        }
        List<LeaveTask> leaveTaskList = new ArrayList<>(taskList.size());
        taskList.forEach(task -> leaveTaskList.add(setLeaveTask(task)));
        return leaveTaskList;
    }

    /**
     * 根据处理人获取已办任务列表
     *
     * @param assignee 处理人
     * @param historyService 已办任务信息接口
     * @author guankai
     * @date 2020/8/17
     * @return java.util.List<org.activiti.engine.history.HistoricTaskInstance>
     **/
    @Override
    public List<HistoricTaskInstance> getDoneTaskList(String assignee,HistoryService historyService) {
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        return (taskInstanceList.isEmpty())?null:taskInstanceList;
    }

    /**
     * 根据流程定义id获取当前活跃节点
     *
     * @param processInstanceId 流程定义id
     * @param taskService 待办任务信息接口
     * @author guankai
     * @date 2020/8/17
     * @return org.activiti.engine.task.Task 当前活跃节点
     **/
    @Override
    public LeaveTask getCurrentTaskByProcessInstanceId(String processInstanceId,TaskService taskService) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        return (task == null)?null:setLeaveTask(task);
    }

    /**
     * 根据任务id获取上一个节点的信息
     *
     * @param taskId 任务id
     * @param historyService 已办任务信息查询接口
     * @author guankai
     * @date 2020/8/17
     * @return org.activiti.engine.history.HistoricTaskInstance
     **/
    @Override
    public HistoricTaskInstance getUpOneTask(String taskId,HistoryService historyService) {
        //上一个节点
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .taskId(taskId).orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        HistoricTaskInstance taskInstance = null;
        if (!list.isEmpty() && list.get(0).getEndTime() != null) {
            taskInstance = list.get(0);
        }
        return taskInstance;
    }

    @Override
    public void handleTask(String processInstanceId,TaskService taskService) {
        LeaveTask currentTask = getCurrentTaskByProcessInstanceId(processInstanceId,taskService);

        List<SequenceFlow> outgoingFlows = currentTask.getOutgoingFlows();
        Task task = taskService.createTaskQuery().taskDefinitionKey(outgoingFlows.get(0).getTargetRef()).singleResult();


        taskService.complete(currentTask.getTaskId());
    }

    /**
     * 封装自定义节点信息
     *
     * @param task 节点信息
     * @author guankai
     * @date 2020/8/17
     * @return com.guankai.activitidemo.model.LeaveTask
     **/
    private LeaveTask setLeaveTask(Task task){
        LeaveTask leaveTask = new LeaveTask();
        leaveTask.setProcessDefinitionId(task.getProcessDefinitionId());
        leaveTask.setProcessInstanceId(task.getProcessInstanceId());
        leaveTask.setTaskId(task.getId());
        leaveTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
        leaveTask.setTaskName(task.getName());
        leaveTask.setAssignee(task.getAssignee());
        leaveTask.setFormKey(task.getFormKey());
        //获取当前模型
        BpmnModel bpmnModel = ProcessEngines.getDefaultProcessEngine().getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
        //获取当前节点
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        //判断节点类型(常用节点类型)
        if (flowElement instanceof StartEvent){
            //开始节点
            StartEvent startEvent = (StartEvent)flowElement;
            leaveTask.setIncomingFlows(startEvent.getIncomingFlows());
            leaveTask.setOutgoingFlows(startEvent.getOutgoingFlows());
            leaveTask.setTaskType(StartEvent.class);
        }else if (flowElement instanceof UserTask){
            //任务节点
            UserTask userTask = (UserTask)flowElement;
            leaveTask.setIncomingFlows(userTask.getIncomingFlows());
            leaveTask.setOutgoingFlows(userTask.getOutgoingFlows());
            leaveTask.setTaskType(UserTask.class);
        }else if (flowElement instanceof ExclusiveGateway){
            //排他网关节点
            ExclusiveGateway exclusiveGateway = (ExclusiveGateway)flowElement;
            leaveTask.setIncomingFlows(exclusiveGateway.getIncomingFlows());
            leaveTask.setOutgoingFlows(exclusiveGateway.getOutgoingFlows());
            leaveTask.setTaskType(ExclusiveGateway.class);
        }else if (flowElement instanceof EndEvent){
            //结束节点
            EndEvent endEvent = (EndEvent)flowElement;
            leaveTask.setIncomingFlows(endEvent.getIncomingFlows());
            leaveTask.setOutgoingFlows(endEvent.getOutgoingFlows());
            leaveTask.setTaskType(EndEvent.class);
        }else {
            leaveTask.setTaskType(null);
        }
        return leaveTask;
    }
}
