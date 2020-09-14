package com.sue.sueactiviti7;

import org.activiti.api.model.shared.model.VariableInstance;

import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;

import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sue
 * @date 2020/9/13 14:38
 */
@SpringBootTest
public class Part8ProcessRuntime {

    @Autowired
    private ProcessRuntime processRuntime;

    @Resource
    private SecurityUtil securityUtil;

//    @Test
//    public void getProcessInstance(){
//        securityUtil.logInAs("bajie");
//        Page<ProcessInstance> processInstancePage = processRuntime.processInstance(Pageable.of(0,100));
//        System.out.println("流程实例数量"+processInstancePage.getTotalItems());
//        List<ProcessInstance> list = processInstancePage.getContent();
//        for(ProcessInstance pi : list){
//            System.out.println(":"+pi.getId());
//            System.out.println(":"+pi.getName());
//            System.out.println(":"+pi.getStartDate());
//            System.out.println(":"+pi.getStatus());
//            System.out.println(":"+pi.getProcessDefinitionId());
//            System.out.println(":"+pi.getProcessDefinitionKey());
//        }
//    }

    @Test
    public void startProcessInstance(){
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.start(
                ProcessPayloadBuilder
                        .start()
                        .withProcessDefinitionKey("defKey")
                        .withName("第一个流程实例名称")
                        .withBusinessKey("自定义key")
                        .build()
        );
    }

    @Test
    public void delProcessInstance(){
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder.delete()
        .withProcessInstanceId("123").build());
    }

    @Test
    public void suspendProcessInstance(){
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.suspend(
                ProcessPayloadBuilder.suspend()
                .withProcessInstanceId("123").build());
    }

    @Test
    public void resumeProcessInstance(){
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.resume(
                ProcessPayloadBuilder.resume()
                        .withProcessInstanceId("123").build());
    }


    @Test
    public void getVariables(){
        securityUtil.logInAs("bajie");
        List<VariableInstance> list = processRuntime.variables(ProcessPayloadBuilder.variables().withProcessInstanceId("").build());
        for(VariableInstance vi:list){
            System.out.println(""+vi.getName());
            System.out.println(""+vi.getValue());
            System.out.println(""+vi.getTaskId());
            System.out.println(""+vi.getProcessInstanceId());
        }
    }
}
