package com.guankai.activitidemo.service;

import com.guankai.activitidemo.vo.ProcessDefinitionVo;
import org.activiti.engine.repository.ProcessDefinition;

import java.io.File;
import java.util.List;

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
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    ProcessDefinition deployProcess(File file);

    /**
     * 查询以部署流程列表信息
     *
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    List<ProcessDefinitionVo> getProcessDefinitionList();

}
