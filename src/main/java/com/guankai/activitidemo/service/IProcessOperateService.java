package com.guankai.activitidemo.service;

import com.guankai.activitidemo.vo.JsonResult;
import com.guankai.activitidemo.vo.ProcessInstanceVo;
import com.guankai.activitidemo.vo.TaskVo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

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
     * @param businessKey 业务编号
     * @author guan.kai
     * @date 2020/8/17
     * @return
     **/
    ProcessInstanceVo startProcessByKey(String processKey, String businessKey);

    /**
     * 获取未完结流程列表
     * 当userId为空时查询所有
     *
     * @param runtimeService
     * @param userId 流程发起人
     * @auhor guankai
     * @date 2020/8/18
     * @return
     **/
    List<ProcessInstanceVo> getUnFinishedProcessList(RuntimeService runtimeService,String userId);

    /**
     * 获取已完结流程列表
     * 当userId为空时查询所有
     *
     * @param historyService 历史信息接口
     * @param userId 流程发起人
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    List<ProcessInstanceVo> getFinishedProcessList(HistoryService historyService,String userId);

    /**
     * 根据处理人获取待办任务列表
     *
     * @param assignee 处理人
     * @param taskService 待办任务处理接口
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    List<TaskVo> getUpComingTaskList(String assignee, TaskService taskService);

    /**
     * 根据处理人获取已办任务列表
     *
     * @param assignee 处理人
     * @param historyService 历史信息接口
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    List<TaskVo> getDoneTaskList(String assignee, HistoryService historyService);


    /**
     * 根据流程定义id获取当前活跃节点
     *
     * @param processInstanceId 流程定义id
     * @param taskService 待办任务处理接口
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    Task getCurrentTaskByProcessInstanceId(String processInstanceId, TaskService taskService);

    /**
     * 获取上一个节点的信息
     *
     * @param processInstanceId 流程实例id
     * @param historyService 历史信息接口
     * @author guankai
     * @date 2020/8/17
     * @return
     **/
    HistoricTaskInstance getUpOneTask(String processInstanceId,HistoryService historyService);

    /**
     * 处理流程
     *
     * @param processInstanceId 流程实例id
     * @author guankai
     * @date 2020/8/18
     * @return
     **/
    JsonResult handleTask(String processInstanceId, String taskId, String fromJson);
}
