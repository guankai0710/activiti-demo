package com.guankai.activitidemo.service;

import com.guankai.activitidemo.vo.ProcessInstanceVo;
import com.guankai.activitidemo.vo.TaskVo;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 类描述：流程操作接口
 *
 * @author guankai
 * @date 2020/8/17
 **/
public interface IProcessOperateService {

    /**
     * 根据流程key启动流程
     *
     * @param processDefinitionId 流程定义id
     * @param businessKey 业务编号
     * @param startUser 流程发起者
     * @param variables 流程变量
     * @author guan.kai
     * @date 2020/8/17
     * @return
     **/
    ProcessInstanceVo startProcessByDefinitionId(String processDefinitionId, String businessKey, String businessName, String startUser,  Map<String,Object> variables);

    /**
     * 获取未完结流程列表
     * 当userId为空时查询所有
     *
     * @param startUser 流程发起人
     * @auhor guankai
     * @date 2020/8/18
     * @return
     **/
    List<ProcessInstanceVo> getUnFinishedProcessList(String startUser);

    /**
     * 获取已完结流程列表
     * 当userId为空时查询所有
     *
     * @param startUser 流程发起人
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    List<ProcessInstanceVo> getFinishedProcessList(String startUser);

    /**
     * 根据处理人获取待办任务列表
     *
     * @param assignee 处理人
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    List<TaskVo> getUpComingTaskList(String assignee);

    /**
     * 根据处理人获取已办任务列表
     *
     * @param assignee 处理人
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    List<TaskVo> getDoneTaskList(String assignee);


    /**
     * 根据流程定义id获取当前活跃节点
     *
     * @param processInstanceId 流程定义id
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    Task getCurrentTaskByProcessInstanceId(String processInstanceId);

    /**
     * 获取上一个节点的信息
     *
     * @param processInstanceId 流程实例id
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    HistoricTaskInstance getUpOneTask(String processInstanceId);

    /**
     * 处理流程
     *
     * @param taskId 任务id
     * @param variables 流程变量(作用范围,整个流程)
     * @param variablesLocal 节点变量(作用范围,当前节点)
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    void handleTask(String taskId, Map<String,Object> variables, Map<String,Object> variablesLocal);

    /**
     * 返回流程图
     *
     * @param processInstanceId 流程实例id
     * @return
     */
    InputStream queryProHighLighted(String processInstanceId) throws IOException;
}
