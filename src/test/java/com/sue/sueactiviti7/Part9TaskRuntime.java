package com.sue.sueactiviti7;


import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author sue
 * @date 2020/9/13 15:33
 */

@SpringBootTest
public class Part9TaskRuntime {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private TaskRuntime taskRuntime;

//    @Test
//    public void getTasks(){
//        securityUtil.logInAs("bajie");
//        Page<Task> taskPage = taskRuntime.task(Pageable.of(0,100));
//        List<Task> list = taskPage.getContent();
//        for(Task tk:list){
//            System.out.println(""+tk.getId());
//            System.out.println(""+tk.getName());
//            System.out.println(""+tk.getStatus());
//            System.out.println(""+tk.getClaimedDate());
//            if(tk.getAssignee()==null){
//                //候选人为当前登录用户,null的时候需要前端拾取
//                System.out.println("待拾取任务");
//            }else{
//                System.out.println(tk.getAssignee());
//            }
//        }
//    }
    @Test
    public void completeTask(){
        securityUtil.logInAs("baijie");
        Task task = taskRuntime.task("");
        if(task.getAssignee()==null){
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
        System.out.println("任务执行");
    }
}
