package com.guankai.activitidemo.service.impl;

import com.guankai.activitidemo.service.IProcessDefinitionService;
import com.guankai.activitidemo.vo.ProcessDefinitionVo;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义、部署
 *
 * @author guan.kai
 * @date 2020/8/16 17:00
 **/
@Service
@Transactional(rollbackFor = Exception.class)
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
    public ProcessDefinition deployProcess(File file) {
        try(InputStream inputStream = new FileInputStream(file)) {
            String filename = file.getName();
            RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
            //部署
            Deployment deployment = repositoryService.createDeployment().addInputStream(filename, inputStream).deploy();
            return repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
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
        if (processDefinitions.isEmpty()){
            return null;
        }
        List<ProcessDefinitionVo> voList = new ArrayList<>(processDefinitions.size());
        processDefinitions.forEach(processDefinition -> {
            ProcessDefinitionVo vo = new ProcessDefinitionVo();
            vo.setId(processDefinition.getId());
            vo.setName(processDefinition.getName());
            vo.setKey(processDefinition.getKey());
            vo.setResourceName(processDefinition.getResourceName());
            vo.setVersion(processDefinition.getVersion());
            voList.add(vo);
        });
        return voList;
    }
}
