package com.guankai.activitidemo.service;

import com.guankai.activitidemo.model.LeaveTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

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
     * @param processKey 流程key
     * @author guan.kai
     * @date 2020/8/17
     * @return 流程实例信息
     **/
    ProcessInstance startProcessByKey(String processKey);

    /**
     * 根据处理人获取待办任务列表
     *
     * @param assignee 处理人
     * @param taskService 待办任务信息接口
     * @author guankai
     * @date 2020/8/17
     * @return java.util.List<org.activiti.api.task.model.Task>
     **/
    List<LeaveTask> getUpComingTaskList(String assignee, TaskService taskService);

    /**
     * 根据处理人获取已办任务列表
     *
     * @param assignee 处理人
     * @param historyService 已办任务信息接口
     * @author guankai
     * @date 2020/8/17
     * @return java.util.List<org.activiti.engine.history.HistoricTaskInstance>
     **/
    List<HistoricTaskInstance> getDoneTaskList(String assignee, HistoryService historyService);


    /**
     * 根据流程定义id获取当前活跃节点
     *
     * @param processInstanceId 流程定义id
     * @param taskService 待办任务信息接口
     * @author guankai
     * @date 2020/8/17
     * @return org.activiti.engine.task.Task 当前活跃节点
     **/
    LeaveTask getCurrentTaskByProcessInstanceId(String processInstanceId,TaskService taskService);

    /**
     * 根据任务id获取上一个节点的信息
     *
     * @param taskId 任务id
     * @param historyService 已办任务信息查询接口
     * @author guankai
     * @date 2020/8/17
     * @return org.activiti.engine.history.HistoricTaskInstance
     **/
    HistoricTaskInstance getUpOneTask(String taskId,HistoryService historyService);

    void handleTask(String processInstanceId,TaskService taskService);
}
