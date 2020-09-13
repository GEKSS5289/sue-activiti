package com.sue.sueactiviti7;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author sue
 * @date 2020/9/13 11:08
 */

@SpringBootTest
public class Part2ProcessDefinition {


    @Autowired
    private RepositoryService repositoryService;

    //查询流程定义
    @Test
    public void getDefinitions(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();
        for(ProcessDefinition pd:list){
            System.out.println(pd.getName());
            System.out.println(pd.getKey());
            System.out.println(pd.getResourceName());
            System.out.println(pd.getDeploymentId());
            System.out.println(pd.getVersion());
        }

    }



    //删除流程定义
    @Test
    public void delDefinition(){
        String pdID = "";

        repositoryService.deleteDeployment(pdID,true);
    }
}
