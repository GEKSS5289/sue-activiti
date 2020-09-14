package com.sue.sueactiviti7;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author sue
 * @date 2020/9/13 12:29
 */

@SpringBootTest
public class Part5HistoryTaskInstance {

    @Autowired
    private HistoryService historyService;

    //根据用户名查询历史记录

    @Test
    public void HistoryTaskInstanceByUser(){
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .taskAssignee("")
                .list();
        for(HistoricTaskInstance hi:historicTaskInstances){

            System.out.println(":"+hi.getId());
            System.out.println(":"+hi.getProcessInstanceId());
            System.out.println(":"+hi.getName());
        }
    }

    @Test
    public void HistoryTaskInstanceByPiID(){
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId("")
                .list();
        for(HistoricTaskInstance hi:list){
            System.out.println();
        }
    }
}
