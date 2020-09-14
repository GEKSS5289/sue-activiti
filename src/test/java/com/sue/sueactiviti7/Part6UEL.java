package com.sue.sueactiviti7;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sue
 * @date 2020/9/13 13:07
 */

@SpringBootTest
public class Part6UEL {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    public void initProcessInstanceWithClassArgs(){

        //流程变量${Zhixingren}
        Map<String,Object> variables = new HashMap<String,Object>();
        variables.put("ZhiXingren","sue");

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myProcess_1","bkey001",variables);
        System.out.println("流程实例ID"+processInstance.getProcessInstanceId());


    }

    @Test
    public void complateTaskWithArgs(){
        Map<String,Object> variables = new HashMap<String,Object>();
        variables.put("ZhiXingren","sue");
        taskService.complete("",variables);
        System.out.println("完成任务");
    }


    @Test
    public void initProcessInstanceWithCandDateArgs(){

    }

    @Test
    public void otherArgs(){

    }

    @Test
    public void otherLocalArgs(){

    }
}
