package com.sue.sueactiviti7;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author sue
 * @date 2020/9/13 11:53
 */

@SpringBootTest
public class Part4Task {


    @Autowired
    private TaskService taskService;

    //查询任务
    @Test
    public void getTasks(){
        List<Task> list = taskService.createTaskQuery().list();
        for(Task tk:list){
            System.out.println(":"+tk.getId());
            System.out.println(":"+tk.getName());
            System.out.println(":"+tk.getAssignee());
        }
    }

    //查询我的代办任务
    @Test
    public void getTaskByAssignee(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("UserTask")
                .list();
        for(Task tk: list){
            System.out.println("id"+tk.getId());
            System.out.println("id"+tk.getName());
            System.out.println("id"+tk.getAssignee());
        }
    }

    //执行任务
    @Test
    public void completeTask(){
        taskService.complete("072bfa9d-f576-11ea-a8eb-0a0027000007");
        System.out.println("完成任务");
    }

    //拾取任务
    public void claimTask(){

    }
}
