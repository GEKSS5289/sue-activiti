package com.sue.sueactiviti7.controller;

import com.sue.sueactiviti7.SecurityUtil;
import com.sue.sueactiviti7.mapper.ActivitiMapper;
import com.sue.sueactiviti7.util.AjaxResponse;
import com.sue.sueactiviti7.util.GlobalConfig;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author sue
 * @date 2020/9/14 12:14
 */

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActivitiMapper activitiMapper;
    //获取我的待办任务
    @GetMapping("/getTasks")
    public AjaxResponse getTasks (){
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }


            Page<Task> tasks = taskRuntime.tasks(Pageable.of(0,100));
            List<HashMap<String,Object>> listMap = new ArrayList<HashMap<String,Object>>();

            for(Task tk:tasks.getContent()){
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("id",tk.getId());
                hashMap.put("name",tk.getName());
                hashMap.put("status",tk.getStatus());
                hashMap.put("createdDate",tk.getCreatedDate());


                if(tk.getAssignee() == null){
                    hashMap.put("assignee","待拾取任务");
                }else{
                    hashMap.put("assignee",tk.getAssignee());
                }

                ProcessInstance processInstance = processRuntime.processInstance(tk.getProcessInstanceId());
                hashMap.put("instanceName",processInstance.getName());




                listMap.add(hashMap);
            }


            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            listMap
                    );


        } catch (Exception e) {
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "获取我的代办任务失败",
                            e.getMessage()
                    );
        }
    }












    //完成任务
    @GetMapping("/completeTask")
    public AjaxResponse completeTask (@RequestParam("taskID")String taskID){
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }

            Task task = taskRuntime.task(taskID);
            if(task.getAssignee() == null){
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }

            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());

            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            null
                    );


        } catch (Exception e) {
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "完成任务失败",
                            e.getMessage()
                    );
        }
    }








    @GetMapping(value = "/formDataShow")
    public AjaxResponse formDataShow(@RequestParam("taskID")String taskID){
        try {
            if(GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);

            //----------构建表单控件历史数据字典-----------
            HashMap<String,String> controlListMap = new HashMap<String,String>();
            //读取数据库本实例下所有表单数据
            List<HashMap<String,Object>> tempControlList = activitiMapper.selectFormData(task.getProcessInstanceId());


            for(HashMap ls:tempControlList){
                controlListMap.put(ls.get("Control_ID_").toString(),ls.get("Control_VALUE_").toString());
            }

            UserTask userTask = (UserTask)repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    .getFlowElement(task.getFormKey());

            List<FormProperty> formProperties = userTask.getFormProperties();

            if(userTask == null){
                return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"没有表单");

            }

            List<FormProperty> formProperties1 = userTask.getFormProperties();
            List<HashMap<String,Object>> listMap = new ArrayList<>();


            for(FormProperty fp:formProperties){

                //fp
                String[] splitFP = fp.getId().split("-_!");
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("id",splitFP[0]);
                hashMap.put("controlType",splitFP[1]);
                hashMap.put("controlLable",splitFP[2]);
//                hashMap.put("controlDefvalue",splitFP[3]);
                hashMap.put("controlParam",splitFP[4]);

                if(splitFP[3].startsWith("FormProperty_")){
                    if(controlListMap.containsKey(splitFP[3])){
                        hashMap.put("controlDefvalue",controlListMap.get(splitFP[3]));
                    }else{
                        hashMap.put("controlDefvalue","读取失败,检查"+splitFP[0]+"配置");
                    }
                }else{
                    hashMap.put("controlDefvalue",splitFP[3]);
                }

                listMap.add(hashMap);
            }

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),listMap);
        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "任务表单渲染失败",
                            e.getMessage()
                    );
        }
    }










    @PostMapping(value = "/formDataSave")
    public AjaxResponse formDataSave(@RequestParam("taskID")String taskID,@RequestParam("formData")String formData){
        try {
            if(GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);


            HashMap<String,Object> variables = new HashMap<>();
            Boolean hasVariables = false;


            String[] formDataList  = formData.split("!_!");
            List<HashMap<String,Object>> listMap = new ArrayList<>();

            for(String controlItem:formDataList){
                String[] formDataItem = controlItem.split("-_!");
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("PROC_DEF_ID_",task.getProcessDefinitionId());
                hashMap.put("PROC_INST_ID_",task.getProcessInstanceId());
                hashMap.put("FORM_KEY_",task.getFormKey());
                hashMap.put("Control_ID_",formDataItem[0]);
                hashMap.put("Control_value_",formDataItem[1]);
//                hashMap.put("Control_PARAM_",formDataItem[2]);
                listMap.add(hashMap);


                //构建参数集合
                switch(formDataItem[2]){
                    case "f":
                        System.out.println("控件值不作为参数");
                        break;
                    case "s":
                        variables.put(formDataItem[0],formDataItem[1]);
                        hasVariables = true;
                        break;
                    case "t":
                        SimpleDateFormat timeFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
                        variables.put(formDataItem[0],timeFormat.parse(formDataItem[1]));
                        hasVariables = true;
                        break;
                    case "b":
                        variables.put(formDataItem[0], BooleanUtils.toBoolean(formDataItem[1]));
                        hasVariables = true;
                        break;
                    default:
                        System.out.println("空间ID:"+formDataItem[0]+"的参数"+formDataItem[2]+"不存在");

                }
            }

            if(hasVariables){
                taskRuntime.complete(
                        //带参数完成任务
                        TaskPayloadBuilder
                                .complete()
                                .withTaskId(taskID)
                                .withVariables(variables)
                                .build()
                );
            }else {
                taskRuntime.complete(
                        TaskPayloadBuilder
                                .complete()
                                .withTaskId(taskID)
                                .build()
                );
            }





            int result = activitiMapper.insertFormData(listMap);

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),listMap);
        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "任务表单提交失败",
                            e.getMessage()
                    );
        }
    }



















}
