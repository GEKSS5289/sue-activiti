package com.sue.sueactiviti7;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author sue
 * @date 2020/9/13 11:21
 */

@SpringBootTest
public class Part3ProcessInstance {

    @Autowired
    private RuntimeService runtimeService;

    //初始化流程实例
    @Test
    public void initProcessInstance(){

        //获取页面表单填报的内容:请假时间 请假事由 string fromData
        //fromData 写入业务表 返回业务表主键id == businesskey
        //把业务数据与Activiti7流程数据关联
        //
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myProcess_1","bkey001");
        System.out.println("流程实例ID"+processInstance.getProcessInstanceId());



    }

    //获取流程实例列表
    @Test
    public void getProcessInstance(){
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .list();
        for(ProcessInstance pd:processInstances){
            System.out.println(""+pd.getProcessInstanceId());
            System.out.println(""+pd.getProcessDefinitionId());
            System.out.println(""+pd.isEnded());
            System.out.println(""+pd.isSuspended());
        }
    }


    //激活与暂停流程实例
    @Test
    public void activitiProcessInstance(){
//        runtimeService.suspendProcessInstanceById("79b44c16-f571-11ea-8b59-0a0027000007");
        runtimeService.activateProcessInstanceById("79b44c16-f571-11ea-8b59-0a0027000007");
    }

    //删除流程实例
    @Test
    public void delProcessInstance(){
        runtimeService.deleteProcessInstance("79b44c16-f571-11ea-8b59-0a0027000007","删玩");
    }




}
