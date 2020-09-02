package com.guankai.activitidemo.service.impl;

import com.guankai.activitidemo.service.IProcessOperateService;
import com.guankai.activitidemo.vo.ProcessInstanceVo;
import com.guankai.activitidemo.vo.TaskVo;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：流程操作接口
 *  流程启动、查询、处理等
 *
 * @author guankai
 * @date 2020/8/17
 **/
@Service
public class ProcessOperateServiceImpl implements IProcessOperateService {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessOperateServiceImpl.class);


    /**
     * 根据流程key启动流程
     *
     * @param processDefinitionId 流程定义id
     * @param businessKey 业务编号
     * @param startUserId 流程发起者
     * @param variables 流程变量
     * @author guan.kai
     * @date 2020/8/17
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstanceVo startProcessByDefinitionId(String processDefinitionId, String businessKey, String businessName, String startUserId, Map<String,Object> variables) {
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        if (StringUtils.isNotBlank(startUserId)){
            //设置流程发起者信息（一般是当前用户id或者用户名等唯一标识）
            Authentication.setAuthenticatedUserId(startUserId);
        }
        //启动流程，返回流程实例信息
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId,businessKey,variables);
        runtimeService.setProcessInstanceName(processInstance.getProcessInstanceId(),businessName);
        //设置流程实例名称
        LOG.info("启动流程 -> 流程key：{}，流程名称：{}，业务编号：{}，实例id：{}"
                ,processInstance.getProcessDefinitionKey(),processInstance.getProcessDefinitionName()
                ,processInstance.getBusinessKey(),processInstance.getProcessInstanceId());
        return processInstanceToVo(processInstance,ProcessEngines.getDefaultProcessEngine().getRepositoryService());
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
        if (list == null || list.isEmpty()){
            return null;
        }
        list.forEach(processInstance -> voList.add(processInstanceToVo(processInstance,ProcessEngines.getDefaultProcessEngine().getRepositoryService())));
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
        if (list == null || list.isEmpty()){
            return null;
        }
        list.forEach(historicProcessInstance -> voList.add(processInstanceToVo(historicProcessInstance,ProcessEngines.getDefaultProcessEngine().getRepositoryService())));
        return voList;
    }

    private ProcessInstanceVo processInstanceToVo(Object obj,RepositoryService repositoryService) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ProcessInstanceVo vo = new ProcessInstanceVo();
        if (obj instanceof HistoricProcessInstance){
            HistoricProcessInstance process = (HistoricProcessInstance)obj;
            BeanUtils.copyProperties(process,vo);
            vo.setStartTime(sdf.format(process.getStartTime()));
            vo.setEndTime(sdf.format(process.getEndTime()));
            vo.setFinished(true);
            processInstanceVoSetStartFormKey(vo,process.getProcessDefinitionId(),repositoryService);
        } else if (obj instanceof ProcessInstance) {
            ProcessInstance process = (ProcessInstance)obj;
            BeanUtils.copyProperties(process,vo);
            vo.setStartTime(sdf.format(process.getStartTime()));
            vo.setEndTime(null);
            vo.setFinished(false);
            processInstanceVoSetStartFormKey(vo,process.getProcessDefinitionId(),repositoryService);
        } else {
            try {
                throw new Exception("参数错误！");
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        }
        return vo;
    }

    private void processInstanceVoSetStartFormKey(ProcessInstanceVo vo,String processDefinitionId,RepositoryService repositoryService){
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Map<String, FlowElement> flowElementMap = bpmnModel.getProcessById(processDefinition.getKey()).getFlowElementMap();
        StartEvent startevent = (StartEvent)flowElementMap.get("startevent");
        String formKey = startevent.getFormKey();
        if (StringUtils.isNotBlank(formKey)){
            vo.setStartFormKey(formKey);
        }
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
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .asc()
                .list();
        if (taskList == null || taskList.isEmpty()){
            return null;
        }
        List<TaskVo> voList = new ArrayList<>(taskList.size());
        taskList.forEach(task -> voList.add(taskToVo(task,runtimeService)));
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
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskAssignee(assignee)
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        if (taskInstanceList == null || taskInstanceList.isEmpty()){
            return null;
        }
        List<TaskVo> voList = new ArrayList<>(taskInstanceList.size());
        taskInstanceList.forEach(historicTaskInstance -> voList.add(taskToVo(historicTaskInstance,runtimeService)));
        return voList;
    }

    private TaskVo taskToVo(TaskInfo taskInfo,RuntimeService runtimeService){
        TaskVo vo = new TaskVo();
        BeanUtils.copyProperties(taskInfo,vo);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(taskInfo.getProcessInstanceId()).singleResult();
        vo.setProcessInstanceName(processInstance.getName());
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
        return (list == null || list.isEmpty())?null:list.get(0);
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
    @Transactional(rollbackFor = Exception.class)
    public void handleTask(String taskId, Map<String,Object> variables, Map<String,Object> variablesLocal) {
        TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
        if (variables != null && !variables.isEmpty()){
            taskService.setVariables(taskId,variables);
        }
        if (variablesLocal != null && !variablesLocal.isEmpty()){
            taskService.setVariablesLocal(taskId,variablesLocal);
        }
        taskService.complete(taskId);
    }

    /**
     * 返回流程图
     *
     * @param processInstanceId 流程实例id
     * @return
     */
    @Override
    public InputStream queryProHighLighted(String processInstanceId) throws IOException {
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null){
            BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            if (model != null && model.getLocationMap().size() > 0) {
                ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
                List<String> executedTaskIdList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list()
                        .stream().map(HistoricTaskInstance::getTaskDefinitionKey).collect(Collectors.toList());
                executedTaskIdList.add("startevent");
                //已执行flow的集和
                List<String> executedFlowIdList = executedFlowIdList(model,
                        historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list());

                return generator.generateDiagram(model, executedTaskIdList, executedFlowIdList, "宋体", "宋体",
                        "宋体", true);
            }
        }
        return null;
    }

    /**
     * 获取已执行flow的集和
     *
     * @param bpmnModel
     * @param historicActivityInstanceList
     * @author guankai
     * @date 2020/8/28
     * @return
     **/
    private List<String> executedFlowIdList(BpmnModel bpmnModel,List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> executedFlowIdList = new ArrayList<>();
        for(int i=0;i<historicActivityInstanceList.size()-1;i++) {
            HistoricActivityInstance hai = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
            List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
            if(sequenceFlows.size()>1) {
                HistoricActivityInstance nextHai = historicActivityInstanceList.get(i+1);
                sequenceFlows.forEach(sequenceFlow->{
                    if(sequenceFlow.getTargetFlowElement().getId().equals(nextHai.getActivityId())) {
                        executedFlowIdList.add(sequenceFlow.getId());
                    }
                });
            }else {
                executedFlowIdList.add(sequenceFlows.get(0).getId());
            }
        }
        return executedFlowIdList;
    }


}
