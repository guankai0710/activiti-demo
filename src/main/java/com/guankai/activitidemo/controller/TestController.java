package com.guankai.activitidemo.controller;

import com.guankai.activitidemo.model.LeaveTask;
import com.guankai.activitidemo.service.IProcessDefinitionService;
import com.guankai.activitidemo.service.IProcessOperateService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

/**
 * 类描述：TODO
 *
 * @author guankai
 * @date 2020/8/17
 **/
@RestController
public class TestController {

    @Autowired
    private IProcessDefinitionService processDefinitionService;
    @Autowired
    private IProcessOperateService processOperateService;

    /**
     * 测试
     * @return
     */
    @GetMapping("/startProcess")
    public String startProcess(){
        //部署流程
        ProcessDefinition processDefinition = processDefinitionService.deployProcess(new File("E:\\processes\\TestProcess.bpmn"));
        //启动流程
        ProcessInstance processInstance = processOperateService.startProcessByKey(processDefinition.getKey());

        //部署流程
        ProcessDefinition processDefinition1 = processDefinitionService.deployProcess(new File("E:\\processes\\LeaveProcess.bpmn"));
        //启动流程
        ProcessInstance processInstance1 = processOperateService.startProcessByKey(processDefinition1.getKey());

        return processInstance1.getId();
    }

    @GetMapping("/getTask")
    public String getTask(@RequestParam(value = "assignee") String assignee,@RequestParam(value = "processInstanceId") String processInstanceId){
        TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
        List<LeaveTask> taskList = processOperateService.getUpComingTaskList(assignee,taskService);
        LeaveTask activeTask = processOperateService.getCurrentTaskByProcessInstanceId(processInstanceId,taskService);
        processOperateService.handleTask(processInstanceId,taskService);
        return null;
    }
}
