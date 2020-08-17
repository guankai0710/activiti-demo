package com.guankai.activitidemo.service.impl;

import com.guankai.activitidemo.service.IProcessDefinitionService;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return 部署对象信息
     **/
    @Override
    public ProcessDefinition deployProcess(File file) {
        try(InputStream inputStream = new FileInputStream(file)) {
            String filename = file.getName();
            //1.获取processEngine（activiti核心的API）
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            //2.获取repositoryService对象（管理流程部署及定义）
            RepositoryService repositoryService = processEngine.getRepositoryService();
            //3.部署
            Deployment deployment = repositoryService.createDeployment().addInputStream(filename,inputStream).deploy();

            //查询刚部署的最新版本流程定义信息
            ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
            //添加查询条件，KEY
            query.deploymentId(deployment.getId());
            //最新版本过滤
            query.latestVersion();
            //查询
            return query.singleResult();
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
            return null;
        }
    }


}
