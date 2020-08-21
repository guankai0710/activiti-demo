//package com.guankai.activitidemo.controller;
//
//import com.guankai.activitidemo.service.IProcessDefinitionService;
//import com.guankai.activitidemo.service.IProcessOperateService;
//import com.guankai.activitidemo.vo.JsonResult;
//import com.guankai.activitidemo.vo.ProcessInstanceVo;
//import com.guankai.activitidemo.vo.TaskVo;
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.ProcessEngines;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.history.HistoricProcessInstanceQuery;
//import org.activiti.engine.repository.ProcessDefinition;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.util.List;
//import java.util.UUID;
//
///**
// * 类描述：TODO
// *
// * @author guankai
// * @date 2020/8/17
// **/
//@RestController
//public class TestController {
//
//    @Autowired
//    private IProcessDefinitionService processDefinitionService;
//    @Autowired
//    private IProcessOperateService processOperateService;
//
//    @GetMapping("/deployProcess")
//    public JsonResult deployProcess(){
//        //部署流程
//
//
//        //部署流程
//
//
//        return null;
//    }
//
//    /**
//     * 测试
//     * @return
//     */
//    @GetMapping("/startProcess")
//    public String startProcess(String processDefinitionKey,String businessKey){
//        //启动流程
//        ProcessInstanceVo leaveProcess = processOperateService.startProcessByDefinitionId(processDefinitionKey, UUID.randomUUID().toString());
//
//        return null;
//    }
//
//    /**
//     * TODO
//     *
//     * @author guankai
//     * @date 2020/8/19
//     * @return
//     **/
//    @GetMapping("/getProcessDefinitionList")
//    public JsonResult getProcessDefinitionList(){
//        return JsonResult.sucess(processDefinitionService.getProcessDefinitionList());
//    }
//
//    @GetMapping("/getTask")
//    public String getTask(@RequestParam(value = "assignee") String assignee,@RequestParam(value = "processInstanceId") String processInstanceId){
//        HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
//        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
//        //未完成流程
//        List<ProcessInstanceVo> unFinishedProcessList = processOperateService.getUnFinishedProcessList(null);
//        //已完成流程
//        List<ProcessInstanceVo> finishedProcessList = processOperateService.getFinishedProcessList(null);
//
////        //待办任务
//        List<TaskVo> taskList = processOperateService.getUpComingTaskList(assignee);
//        //已办任务
//        List<TaskVo> doneTaskList = processOperateService.getDoneTaskList(assignee);
////        当前流程待处理任务
//        Task activeTask = processOperateService.getCurrentTaskByProcessInstanceId(processInstanceId);
//
//
////        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
////        BpmnModel bpmnModel = repositoryService.getBpmnModel(activeTask.getProcessDefinitionId());
////        UserTask userTask = (UserTask)bpmnModel.getFlowElement(activeTask.getTaskDefinitionKey());
////        List<SequenceFlow> userTaskOutgoingFlows = userTask.getOutgoingFlows();
////        userTaskOutgoingFlows.forEach(userTaskSequenceFlow -> {
////            FlowElement targetFlowElement = userTaskSequenceFlow.getTargetFlowElement();
////            if (targetFlowElement instanceof ExclusiveGateway){
////                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) targetFlowElement;
////                List<SequenceFlow> exclusiveGatewayOutgoingFlows = exclusiveGateway.getOutgoingFlows();
////                exclusiveGatewayOutgoingFlows.forEach(exclusiveGatewayFlow -> {
////                    String targetRef = exclusiveGatewayFlow.getTargetRef();
////                    String conditionExpression = exclusiveGatewayFlow.getConditionExpression();
////                    System.out.println(conditionExpression);
////                });
////            }
////        });
//
//
////        //当前流程上一节点
////        if (activeTask != null){
////            HistoricTaskInstance upOneTask = processOperateService.getUpOneTask(activeTask.getProcessInstanceId(), historyService);
////        }
//
//        return null;
//    }
//
//    @GetMapping("/handleTask")
//    public String handleTask(@RequestParam(value = "processInstanceId") String processInstanceId){
//        processOperateService.handleTask(processInstanceId,"","");
//        return null;
//    }
//}
