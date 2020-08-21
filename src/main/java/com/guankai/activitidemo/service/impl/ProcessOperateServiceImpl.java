package com.guankai.activitidemo.service.impl;

import com.guankai.activitidemo.service.IProcessOperateService;
import com.guankai.activitidemo.vo.JsonResult;
import com.guankai.activitidemo.vo.ProcessInstanceVo;
import com.guankai.activitidemo.vo.TaskVo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
     * @param processDefinitionId 流程定义id
     * @param businessKey 业务编号
     * @param userId 流程发起者
     * @param variables 流程变量
     * @author guan.kai
     * @date 2020/8/17
     * @return
     **/
    @Override
    public ProcessInstanceVo startProcessByDefinitionId(String processDefinitionId, String businessKey, String userId, Map<String,Object> variables) {
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        if (StringUtils.isNotBlank(userId)){
            //设置流程发起者信息（一般是当前用户id或者用户名等唯一标识）
            Authentication.setAuthenticatedUserId(userId);
        }
        //启动流程，返回流程实例信息
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId,businessKey,variables);
        //设置流程实例名称
        runtimeService.setProcessInstanceName(processInstance.getId(),"实例名称");
        LOG.info("启动流程 -> 流程key：{}，流程名称：{}，业务编号：{}，实例id：{}"
                ,processInstance.getProcessDefinitionKey(),processInstance.getProcessDefinitionName()
                ,processInstance.getBusinessKey(),processInstance.getProcessInstanceId());
        return processInstanceToVo(processInstance);
    }

    /**
     * 获取所有未完结流程列表
     * 当userId为空时查询所有
     *
     * @param userId 流程发起人
     * @auhor guankai
     * @date 2020/8/18
     * @return
     **/
    @Override
    public List<ProcessInstanceVo> getUnFinishedProcessList(String userId) {
        List<ProcessInstanceVo> voList = new ArrayList<>();
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
        if (StringUtils.isNotBlank(userId)){
            query = query.startedBy(userId);
        }
        List<ProcessInstance> list = query.list();
        if (list.isEmpty()){
            return null;
        }
        list.forEach(processInstance -> voList.add(processInstanceToVo(processInstance)));
        return voList;
    }

    /**
     * 获取已完结流程列表
     * 当userId为空时查询所有
     *
     * @param userId 流程发起人
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    @Override
    public List<ProcessInstanceVo> getFinishedProcessList(String userId) {
        List<ProcessInstanceVo> voList = new ArrayList<>();
        HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if (StringUtils.isNotBlank(userId)){
            query = query.startedBy(userId);
        }
        List<HistoricProcessInstance> list = query.finished().orderByProcessInstanceEndTime().desc().list();
        if (list.isEmpty()){
            return null;
        }
        list.forEach(historicProcessInstance -> voList.add(processInstanceToVo(historicProcessInstance)));
        return voList;
    }

    private ProcessInstanceVo processInstanceToVo(Object obj) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ProcessInstanceVo vo = new ProcessInstanceVo();
        if (obj instanceof HistoricProcessInstance){
            HistoricProcessInstance process = (HistoricProcessInstance)obj;
            BeanUtils.copyProperties(process,vo);
            vo.setStartTime(sdf.format(process.getStartTime()));
            vo.setEndTime(sdf.format(process.getEndTime()));
            vo.setFinished(true);
        } else if (obj instanceof ProcessInstance) {
            ProcessInstance process = (ProcessInstance)obj;
            BeanUtils.copyProperties(process,vo);
            vo.setStartTime(sdf.format(process.getStartTime()));
            vo.setEndTime(null);
            vo.setFinished(false);
        } else {
            try {
                throw new Exception("参数错误！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vo;
    }

    /**
     * 根据处理人获取待办任务列表
     *
     * @param assignee 处理人
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    @Override
    public List<TaskVo> getUpComingTaskList(String assignee) {
        TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .asc()
                .list();
        if (taskList.isEmpty()){
            return null;
        }
        List<TaskVo> voList = new ArrayList<>(taskList.size());
        taskList.forEach(task -> voList.add(taskToVo(task)));
        return voList;
    }

    /**
     * 根据处理人获取已办任务列表
     *
     * @param assignee 处理人
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    @Override
    public List<TaskVo> getDoneTaskList(String assignee) {
        HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskAssignee(assignee)
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        if (taskInstanceList.isEmpty()){
            return null;
        }
        List<TaskVo> voList = new ArrayList<>(taskInstanceList.size());
        taskInstanceList.forEach(historicTaskInstance -> voList.add(taskToVo(historicTaskInstance)));
        return voList;
    }

    private TaskVo taskToVo(TaskInfo taskInfo){
        TaskVo vo = new TaskVo();
        BeanUtils.copyProperties(taskInfo,vo);
        return vo;
    }

    /**
     * 根据流程定义id获取当前活跃节点
     *
     * @param processInstanceId 流程定义id
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    @Override
    public Task getCurrentTaskByProcessInstanceId(String processInstanceId) {
        TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
        return taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
    }

    /**
     * 获取上一个节点的信息
     *
     * @param processInstanceId 流程实例id
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    @Override
    public HistoricTaskInstance getUpOneTask(String processInstanceId) {
        HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        return (list.isEmpty())?null:list.get(0);
    }

    /**
     * 处理流程
     *
     * @param taskId 任务id
     * @param variables 流程变量(作用范围,整个流程)
     * @param variablesLocal 节点变量(作用范围,当前节点)
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    @Override
    public JsonResult handleTask(String taskId, Map<String,Object> variables, Map<String,Object> variablesLocal) {
        TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();

//        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
        //当前节点流信息
//        UserTask userTask = (UserTask)bpmnModel.getFlowElement(currentTask.getTaskDefinitionKey());
//        SequenceFlow userTaskSequenceFlow = userTask.getOutgoingFlows().get(0);
//        FlowElement targetFlowElement = userTaskSequenceFlow.getTargetFlowElement();
//        //判断下一节点是否为网关
//        if (targetFlowElement instanceof ExclusiveGateway){
//            ExclusiveGateway exclusiveGateway = (ExclusiveGateway) targetFlowElement;
//            List<SequenceFlow> exclusiveGatewayOutgoingFlows = exclusiveGateway.getOutgoingFlows();
//            exclusiveGatewayOutgoingFlows.forEach(exclusiveGatewayFlow -> {
//                //条件表达式
//                String conditionExpression = exclusiveGatewayFlow.getConditionExpression();
//
//            });
//        }
        if (!variables.isEmpty()){
            taskService.setVariables(taskId,variables);
        }
        if (!variablesLocal.isEmpty()){
            taskService.setVariablesLocal(taskId,variablesLocal);
        }
        taskService.complete(taskId);
        return JsonResult.sucess("处理成功！",null);
    }

//    /**
//     * 封装自定义节点信息
//     *
//     * @param task 节点信息
//     * @author guankai
//     * @date 2020/8/17
//     * @return com.guankai.activitidemo.model.LeaveTask
//     **/
//    private LeaveTask setLeaveTask(Task task){
//        LeaveTask leaveTask = new LeaveTask();
//        leaveTask.setProcessDefinitionId(task.getProcessDefinitionId());
//        leaveTask.setProcessInstanceId(task.getProcessInstanceId());
//        leaveTask.setTaskId(task.getId());
//        leaveTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
//        leaveTask.setTaskName(task.getName());
//        leaveTask.setAssignee(task.getAssignee());
//        leaveTask.setFormKey(task.getFormKey());
//        //获取当前模型
//        BpmnModel bpmnModel = ProcessEngines.getDefaultProcessEngine().getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
//        //获取当前节点
//        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
//        //判断节点类型(常用节点类型)
//        if (flowElement instanceof StartEvent){
//            //开始节点
//            StartEvent startEvent = (StartEvent)flowElement;
//            leaveTask.setIncomingFlows(startEvent.getIncomingFlows());
//            leaveTask.setOutgoingFlows(startEvent.getOutgoingFlows());
//            leaveTask.setTaskType(StartEvent.class);
//        }else if (flowElement instanceof UserTask){
//            //任务节点
//            UserTask userTask = (UserTask)flowElement;
//            leaveTask.setIncomingFlows(userTask.getIncomingFlows());
//            leaveTask.setOutgoingFlows(userTask.getOutgoingFlows());
//            leaveTask.setTaskType(UserTask.class);
//        }else if (flowElement instanceof ExclusiveGateway){
//            //排他网关节点
//            ExclusiveGateway exclusiveGateway = (ExclusiveGateway)flowElement;
//            leaveTask.setIncomingFlows(exclusiveGateway.getIncomingFlows());
//            leaveTask.setOutgoingFlows(exclusiveGateway.getOutgoingFlows());
//            leaveTask.setTaskType(ExclusiveGateway.class);
//        }else if (flowElement instanceof EndEvent){
//            //结束节点
//            EndEvent endEvent = (EndEvent)flowElement;
//            leaveTask.setIncomingFlows(endEvent.getIncomingFlows());
//            leaveTask.setOutgoingFlows(endEvent.getOutgoingFlows());
//            leaveTask.setTaskType(EndEvent.class);
//        }else {
//            leaveTask.setTaskType(null);
//        }
//        return leaveTask;
//    }


    /**
     * 返回流程图
     *
     * @param processInstanceId 流程实例id
     * @param highlight 是否高亮显示
     * @return
     */
    @Override
    public InputStream queryProHighLighted(String processInstanceId,boolean highlight) {
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null){
            BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            if (model != null && model.getLocationMap().size() > 0) {
                ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
                // 生成流程图 已启动的task 高亮显示
                return generator.generateDiagram(model, highlight?runtimeService.getActiveActivityIds(processInstanceId):Collections.<String>emptyList());
                // 生成流程图 都不高亮
//                return generator.generateDiagram(model, Collections.<String>emptyList());
            }

        }
        return null;
    }

}
