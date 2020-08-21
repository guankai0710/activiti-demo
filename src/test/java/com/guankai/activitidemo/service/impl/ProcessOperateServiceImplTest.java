package com.guankai.activitidemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.guankai.activitidemo.service.IProcessOperateService;
import com.guankai.activitidemo.vo.ProcessInstanceVo;
import com.guankai.activitidemo.vo.TaskVo;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProcessOperateServiceImplTest {

    @Autowired
    private IProcessOperateService processOperateService;


    /**
     * 启动流程
     */
    @Test
    void startProcessByKeyTest() {
        String processDefinitionId = "LeaveProcess:1:ab8476b1-e35d-11ea-a244-98541b270ceb";
        String businessKey = "业务key11112222333344445555";
        String userId = "guankai";
        Map<String,Object> variables = new HashMap<>();
        variables.put("status",3);
        ProcessInstanceVo processInstanceVo = processOperateService.startProcessByDefinitionId(processDefinitionId,businessKey,userId,variables);
        System.out.println();
    }

    @Test
    void getUnFinishedProcessListTest() {
        List<ProcessInstanceVo> unFinishedProcessList = processOperateService.getUnFinishedProcessList(null);
        System.out.println();
    }

    @Test
    void getFinishedProcessListTest() {
        List<ProcessInstanceVo> finishedProcessList = processOperateService.getFinishedProcessList(null);
        System.out.println();
    }

    @Test
    void getUpComingTaskListTest() {
        List<TaskVo> taskList = processOperateService.getUpComingTaskList("guankai");
        System.out.println();
    }

    @Test
    void getDoneTaskListTest() {
        List<TaskVo> taskList = processOperateService.getDoneTaskList("guankai");
        System.out.println();
    }

    /**
     * 处理流程
     */
    @Test
    void handleTaskTest() {
        //当前节点
        Task currentTask = processOperateService.getCurrentTaskByProcessInstanceId("fc4a68b3-e35d-11ea-87bc-98541b270ceb");

        if (currentTask!=null && "taskId".equals(currentTask.getId())){
            String fromJson = "";//表单填写内容
            Map map = JSON.parseObject(fromJson, Map.class);

            Map<String,Object> variables = new HashMap<>();
            variables.put("status",4);


            processOperateService.handleTask(currentTask.getId(),variables,map);
        }else {

        }



    }

    @Test
    void queryProHighLightedTest() {
        InputStream inputStream = processOperateService.queryProHighLighted("fc4a68b3-e35d-11ea-87bc-98541b270ceb", false);
        System.out.println();
    }
}