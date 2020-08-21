package com.guankai.activitidemo.service.impl;

import com.guankai.activitidemo.service.IProcessDefinitionService;
import com.guankai.activitidemo.vo.ProcessDefinitionVo;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProcessDefinitionServiceImplTest {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    /**
     * 部署流程测试
     */
    @Test
    void deployProcessTest() {
        ProcessDefinition processDefinition = processDefinitionService.deployProcess(new File("E:\\processes\\EasyProcess.bpmn"));
        ProcessDefinition processDefinition1 = processDefinitionService.deployProcess(new File("E:\\processes\\LeaveProcess.bpmn"));
        System.out.println();
    }

    /**
     * 获取已部署的流程
     */
    @Test
    void getProcessDefinitionListTest() {
        List<ProcessDefinitionVo> processDefinitionList = processDefinitionService.getProcessDefinitionList();
        System.out.println();
    }
}