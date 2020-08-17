package com.guankai.activitidemo.service;

import org.activiti.engine.repository.ProcessDefinition;

import java.io.File;

/**
 * 流程定义、部署
 *
 * @author guan.kai
 * @date 2020/8/16 16:59
 **/
public interface IProcessDefinitionService {

    /**
     * 部署流程文件
     *
     * @param file 流程文件
     * @author guan.kai
     * @date 2020/8/16
     * @return 流程定义信息
     **/
    ProcessDefinition deployProcess(File file);



}
