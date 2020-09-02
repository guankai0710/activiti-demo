//package com.guankai.activitidemo.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.guankai.activitidemo.dao.ActFromContentDao;
//import com.guankai.activitidemo.entity.ActFromContent;
//import com.guankai.activitidemo.enumerate.StatusEnum;
//import com.guankai.activitidemo.service.IProcessOperateService;
//import com.guankai.activitidemo.vo.ProcessInstanceVo;
//import com.guankai.activitidemo.vo.TaskVo;
//import org.activiti.engine.task.Task;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@WebAppConfiguration
//class ProcessOperateServiceImplTest {
//
//    @Autowired
//    private IProcessOperateService processOperateService;
//    @Autowired
//    private ActFromContentDao actFromContentDao;
//
//
//    /**
//     * 启动流程
//     */
//    @Test
//    void startProcessByKeyTest() {
//        String processDefinitionId = "easyProcess:1:447f7091-e8f7-11ea-9fe6-98541b270ceb";
//        String businessKey = "业务描述"+ UUID.randomUUID();
//        String userId = "faqiren"; //流程发起人
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("status", StatusEnum.HANDLE.getValue());  //流程状态
//        variables.put("userId","guankai"); //下一节点处理人
//        //启动流程
//        ProcessInstanceVo processInstanceVo = processOperateService.startProcessByDefinitionId(processDefinitionId,businessKey,userId,variables);
////        //表单数据存库
////        ActFromContent actFromContent = new ActFromContent();
////        actFromContent.setProcInstId(processInstanceVo.getId());
////        actFromContent.setFromKey(processInstanceVo.getStartFormKey());
////        actFromContent.setContent("from_json_str");
////        actFromContentDao.insertSelective(actFromContent);
//        System.out.println();
//    }
//
//    @Test
//    void getUnFinishedProcessListTest() {
//        List<ProcessInstanceVo> unFinishedProcessList = processOperateService.getUnFinishedProcessList("guankai");
//        System.out.println();
//    }
//
//    @Test
//    void getFinishedProcessListTest() {
//        List<ProcessInstanceVo> finishedProcessList = processOperateService.getFinishedProcessList(null);
//        System.out.println();
//    }
//
//    @Test
//    void getUpComingTaskListTest() {
//        List<TaskVo> taskList = processOperateService.getUpComingTaskList("ceshi");
//        System.out.println();
//    }
//
//    @Test
//    void getDoneTaskListTest() {
//        List<TaskVo> taskList = processOperateService.getDoneTaskList("guankai");
//        System.out.println();
//    }
//
//    /**
//     * 处理流程
//     */
//    @Test
//    void handleTaskTest() {
//        //当前节点
//        Task currentTask = processOperateService.getCurrentTaskByProcessInstanceId("28a6977c-e769-11ea-b5a5-98541b270ceb");
//
//        Map<String,Object> variablesLocal = new HashMap<>();
//        variablesLocal.put("day",4);  //节点变量
//        variablesLocal.put("userId","ceshi");   //下一节点处理人
//
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("status",StatusEnum.REVIEW.getValue());  //流程状态
//        /* 由于工作流引擎不涉及流程业务状态，可在每个任务节点表单设置处理状态，以流程变量的形式存入工作流引擎中 */
//        processOperateService.handleTask(currentTask.getId(),variables,variablesLocal);
//
////         if (currentTask!=null && "taskId".equals(currentTask.getId())){
////                String fromJson = "from_json_str";  //表单填写内容
////                Map variablesLocal = JSON.parseObject(fromJson, Map.class);
////
//////            Map<String,Object> variablesLocal = new HashMap<>();
//////            variablesLocal.put("day",4);  //节点变量
//////            variablesLocal.put("userId","ceshi");   //下一节点处理人
////
////            Map<String,Object> variables = new HashMap<>();
////            variables.put("status",StatusEnum.REVIEW.getValue());  //流程状态
////            /* 由于工作流引擎不涉及流程业务状态，可在每个任务节点表单设置处理状态，以流程变量的形式存入工作流引擎中 */
////            processOperateService.handleTask(currentTask.getId(),variables,variablesLocal);
////
////        }else {
////             System.out.println("任务不存在");
////        }
//
//
//
//    }
//
//}