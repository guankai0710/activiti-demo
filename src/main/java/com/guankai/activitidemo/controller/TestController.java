package com.guankai.activitidemo.controller;

import com.alibaba.fastjson.JSON;
import com.guankai.activitidemo.dao.ActFromContentDao;
import com.guankai.activitidemo.entity.ActFromContent;
import com.guankai.activitidemo.enumerate.StatusEnum;
import com.guankai.activitidemo.service.IProcessDefinitionService;
import com.guankai.activitidemo.service.IProcessOperateService;
import com.guankai.activitidemo.vo.JsonResult;
import com.guankai.activitidemo.vo.ProcessDefinitionVo;
import com.guankai.activitidemo.vo.ProcessInstanceVo;
import com.guankai.activitidemo.vo.TaskVo;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 类描述：TODO
 *
 * @author guankai
 * @date 2020/8/17
 **/
@RestController
@RequestMapping("/activiti")
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private IProcessOperateService processOperateService;
    @Autowired
    private IProcessDefinitionService processDefinitionService;
    @Autowired
    private ActFromContentDao actFromContentDao;

    /**
     * 部署流程
     *
     * @param file 流程文件
     * @author guankai
     * @date 2020/8/31
     * @return void
     **/
    @PostMapping("/deployProcess")
    public JsonResult deployProcess(MultipartFile file){
//        ProcessDefinitionVo processDefinition = processDefinitionService.deployProcess(new File("E:\\processes\\ProcessDemo.bpmn"));
        ProcessDefinitionVo processDefinition = processDefinitionService.deployProcess(file);
        return JsonResult.sucess(processDefinition);
    }

    /**
     * 获取已部署的流程
     */
    @GetMapping("/getProcessDefinitionList")
    public JsonResult getProcessDefinitionList(){
        List<ProcessDefinitionVo> processDefinitionList = processDefinitionService.getProcessDefinitionList();
        return JsonResult.sucess(processDefinitionList);
    }

    /**
     * 启动流程
     *
     * @param processDefinitionId 流程部署信息id
     * @param startFromJson 开始节点表单数据json字符串
     * @param businessKey 业务key
     * @param businessName 业务名称
     */
    @PostMapping("/startProcessByDefinitionId")
    public JsonResult startProcessByDefinitionId(String processDefinitionId,String startFromJson,String businessKey, String businessName){
        //测试数据
//        startFromJson = "{\"alarmContent\":\"大口径水表开关量告警\",\"alarmDuration\":48960,\"alarmTime\":\"2020-05-28 12:07:26\",\"userId\":\"guankai\"}";
//        processDefinitionId = "ProcessDemo:1:ea2a7830-ece1-11ea-a16a-98541b270ceb";
//        businessKey = UUID.randomUUID().toString();
//        businessName = "测试业务";


        //解析表单数据
        HashMap startFromMap = JSON.parseObject(startFromJson, HashMap.class);
        String nextAssignee = (String)startFromMap.get("userId");
        if (StringUtils.isBlank(nextAssignee)){
            return JsonResult.failure("启动失败，为指定处理人");
        }
        startFromMap.remove("userId");
        //流程发起人
        String startAssignee = "guankai";
        //流程变量
        Map<String,Object> variables = new HashMap<>(16);
        variables.put("userId",nextAssignee);
        // 根据不同的开始表单进行不同的业务处理
        ProcessDefinitionVo processDefinitionVo = processDefinitionService.getByProcessDefinitionId(processDefinitionId);
        switch (processDefinitionVo.getStartFormKey()){
            case "leave_start_from":
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                //流程状态
                variables.put("status", StatusEnum.HANDLE.getValue());break;
            case "easy_start_from":
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                //流程状态
                variables.put("status", StatusEnum.HANDLE.getValue());break;
            case "test_start_from":
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                //流程状态
                variables.put("status", StatusEnum.REVIEW.getValue());break;
            default:
                break;
        }

        //启动流程
        ProcessInstanceVo processInstanceVo = processOperateService.startProcessByDefinitionId(processDefinitionId,businessKey,businessName,startAssignee,variables);
        ActFromContent content = new ActFromContent();
        content.setProcInstId(processInstanceVo.getId());
        content.setFromKey(processInstanceVo.getStartFormKey());
        content.setContent(startFromJson);
        content.setCreatePerson(startAssignee);
        content.setCreateTime(new Date());
        actFromContentDao.insertSelective(content);

        return JsonResult.sucess(processInstanceVo);
    }

    /**
     * 获取当前用户待办任务
     */
    @GetMapping("/getUpComingTaskList")
    public JsonResult getUpComingTaskList(){
        String assignee = "guankai";
        List<TaskVo> taskList = processOperateService.getUpComingTaskList(assignee);
        return JsonResult.sucess(taskList);
    }

    /**
     * 处理流程
     * @param processInstanceId 流程实例id
     * @param taskId 任务节点id
     * @param fromJson 表单数据json字符串
     */
    @PostMapping("/handleTask")
    public JsonResult handleTask(String processInstanceId,String taskId, String fromJson){
//        fromJson = "{\"result\":1,\"handleTime\":\"2020-05-28 12:07:26\",\"userId\":\"guankai\",\"handleOpinion\":\"暂无意见\"}";
        //解析表单数据（包含下一节点处理人、条件表达式变量）
        HashMap fromMap = JSON.parseObject(fromJson, HashMap.class);
        String nextAssignee = (String)fromMap.get("userId");
        if (StringUtils.isBlank(nextAssignee)){
            return JsonResult.failure("处理失败，为指定处理人");
        }
        //流程变量
        Map<String,Object> variables = new HashMap<>(16);
        //当前节点
        Task currentTask = processOperateService.getCurrentTaskByProcessInstanceId(processInstanceId);
        if (currentTask == null || !taskId.equals(currentTask.getId())){
            return JsonResult.failure("任务不存在");
        }
        //根据不同的表单进行不同的业务处理
        switch (currentTask.getFormKey()){
            case "demo_review_from":
                //流程状态
                variables.put("status",StatusEnum.REVIEW.getValue());
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                ActFromContent content = new ActFromContent();
                content.setProcInstId(currentTask.getProcessInstanceId());
                content.setTaskId(currentTask.getId());
                content.setFromKey(currentTask.getFormKey());
                content.setContent(fromJson);
                content.setCreatePerson("guankai");
                content.setCreateTime(new Date());
                actFromContentDao.insertSelective(content);
                break;
            case "demo_handle_from":
                //流程状态
                variables.put("status",StatusEnum.REVIEW.getValue());
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                break;
            case "leave_leader_handle_form":
                //流程状态
                variables.put("status",StatusEnum.REVIEW.getValue());
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                break;
            case "leave_manager_review_form":
                //流程状态
                variables.put("status",StatusEnum.COMPLETED.getValue());
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                break;
            case "leave_director_review_form":
                //流程状态
                variables.put("status",StatusEnum.COMPLETED.getValue());
                // TODO 根据 businessKey 修改对应的业务数据，比如业务表中的流程状态等
                break;
            default:
                break;
        }
        processOperateService.handleTask(currentTask.getId(),variables,fromMap);

        return JsonResult.sucess();
    }

    /**
     * 获取流程图高亮显示
     *
     * @param response 响应体
     * @param processInstanceId 流程实例id
     * @author guankai
     * @date 2020/8/31
     * @return void
     **/
    @GetMapping("/queryProHighLighted")
    public void queryProHighLighted(HttpServletResponse response,String processInstanceId){
        try(InputStream is = processOperateService.queryProHighLighted(processInstanceId);
            OutputStream os = response.getOutputStream()){
            // 图片文件流缓存池
            byte [] buffer = new byte[1024];
            while(is.read(buffer) != -1){
                os.write(buffer);
            }
            os.flush();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


}
