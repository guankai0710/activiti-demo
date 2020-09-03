package com.guankai.activitidemo.service.impl;

import com.guankai.activitidemo.service.IProcessDefinitionService;
import com.guankai.activitidemo.vo.ProcessDefinitionVo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程定义、部署
 *
 * @author guan.kai
 * @date 2020/8/16 17:00
 **/
@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessDefinitionServiceImpl.class);


    /**
     * 部署流程文件
     *
     * @param file 流程文件
     * @author guan.kai
     * @date 2020/8/16
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessDefinitionVo deployProcess(MultipartFile file) {
        try(InputStream inputStream = file.getInputStream()) {
            String filename = file.getOriginalFilename();
            RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
            //部署
            Deployment deployment = repositoryService.createDeployment().addInputStream(filename, inputStream).deploy();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            return processDefinitionToVo(processDefinition,repositoryService.getBpmnModel(processDefinition.getId()));
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 查询以部署流程列表信息
     *
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    @Override
    public List<ProcessDefinitionVo> getProcessDefinitionList() {
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionId()
                .asc()
                .list();
        if (processDefinitions == null || processDefinitions.isEmpty()){
            return null;
        }
        List<ProcessDefinitionVo> voList = new ArrayList<>(processDefinitions.size());
        processDefinitions.forEach(processDefinition -> voList.add(processDefinitionToVo(processDefinition,repositoryService.getBpmnModel(processDefinition.getId()))));
        return voList;
    }

    private ProcessDefinitionVo processDefinitionToVo(ProcessDefinition processDefinition,BpmnModel bpmnModel){
        ProcessDefinitionVo vo = new ProcessDefinitionVo();
        vo.setId(processDefinition.getId());
        vo.setName(processDefinition.getName());
        vo.setKey(processDefinition.getKey());
        vo.setResourceName(processDefinition.getResourceName());
        vo.setVersion(processDefinition.getVersion());
        Map<String, FlowElement> flowElementMap = bpmnModel.getProcessById(processDefinition.getKey()).getFlowElementMap();
        StartEvent startevent = (StartEvent)flowElementMap.get("startevent");
        String formKey = startevent.getFormKey();
        if (StringUtils.isNotBlank(formKey)){
            vo.setStartFormKey(formKey);
        }
        return vo;
    }

    /**
     * 查询流程部署信息
     * @param processDefinitionId
     * @return
     */
    @Override
    public ProcessDefinitionVo getByProcessDefinitionId(String processDefinitionId) {
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return processDefinitionToVo(processDefinition,repositoryService.getBpmnModel(processDefinition.getId()));
    }
}
