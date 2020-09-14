package com.sue.sueactiviti7.controller;

import com.sue.sueactiviti7.SecurityUtil;
import com.sue.sueactiviti7.pojo.UserInfo;
import com.sue.sueactiviti7.util.AjaxResponse;
import com.sue.sueactiviti7.util.GlobalConfig;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author sue
 * @date 2020/9/14 11:03
 */

@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;



    //查询流程实例

    @GetMapping("/getInstances")
    public AjaxResponse getInstances(@AuthenticationPrincipal UserInfo userInfo) {
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }

            Page<ProcessInstance> processInstances = processRuntime.processInstances(Pageable.of(0, 100));
            List<ProcessInstance> list = processInstances.getContent();
            list.sort((y, x) -> x.getStartDate().toString().compareTo(y.getStartDate().toString()));


            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

            for (ProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pi.getId());
                hashMap.put("name", pi.getName());
                hashMap.put("status", pi.getStatus());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                hashMap.put("startDate", pi.getStartDate());
                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());


                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();
                hashMap.put("deploymentId", pd.getDeploymentId());
                hashMap.put("resourceName", pd.getResourceName());

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
                            "获取流程实例失败",
                            e.getMessage()
                    );
        }
    }




















    //启动流程实例
    @GetMapping("/startProcess")
    public AjaxResponse startProcess (
            @RequestParam("processDefinitionKey") String processDefinitionKey,
            @RequestParam("instanceName") String instanceName
    ){
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            } else {
                securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
            }

            ProcessInstance processInstance = processRuntime.start(
                    ProcessPayloadBuilder
                            .start()
                            .withProcessDefinitionKey(processDefinitionKey)
                            .withName(instanceName)
                            .withBusinessKey("自定义BusinessKey")
                            .build()
            );

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
                            "启动流程实例失败",
                            e.getMessage()
                    );
        }
    }









    //挂起流程实例

    @GetMapping("/suspendInstance")
    public AjaxResponse suspendInstance (
            @RequestParam("instanceID") String instanceID
    ){
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }


            ProcessInstance processInstance = processRuntime.suspend(
                    ProcessPayloadBuilder
                            .suspend()
                            .withProcessInstanceId(instanceID)
                            .build()
            );

            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            processInstance.getName()
                    );


        } catch (Exception e) {
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "挂起流程实例失败",
                            e.getMessage()
                    );
        }
    }





    //激活流程实例
    @GetMapping("/resumeInstance")
    public AjaxResponse resumeInstance (
            @RequestParam("instanceID") String instanceID
    ){
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }


            ProcessInstance processInstance = processRuntime.resume(
                    ProcessPayloadBuilder
                            .resume()
                            .withProcessInstanceId(instanceID)
                            .build()
            );

            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            processInstance.getName()
                    );


        } catch (Exception e) {
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "激活流程实例失败",
                            e.getMessage()
                    );
        }
    }






    //删除流程实例

    @GetMapping("/deleteInstance")
    public AjaxResponse deleteInstance (
            @RequestParam("instanceID") String instanceID
    ){
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }


            ProcessInstance processInstance = processRuntime.delete(
                    ProcessPayloadBuilder
                            .delete()
                            .withProcessInstanceId(instanceID)
                            .build()
            );

            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            processInstance.getName()
                    );


        } catch (Exception e) {
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "删除流程实例失败",
                            e.getMessage()
                    );
        }
    }










    //查询流程参数

    @GetMapping("/variables")
    public AjaxResponse variables (
            @RequestParam("instanceID") String instanceID
    ){
        try {

            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }


            List<VariableInstance> variables = processRuntime.variables(
                    ProcessPayloadBuilder
                            .variables()
                            .withProcessInstanceId(instanceID)
                            .build()
            );

            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            variables
                    );


        } catch (Exception e) {
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "查询流程实例参数失败",
                            e.getMessage()
                    );
        }
    }

















}

