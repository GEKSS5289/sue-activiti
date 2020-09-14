package com.sue.sueactiviti7;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author sue
 * @date 2020/9/13 10:14
 */

@SpringBootTest
public class Part1Deployment {

    @Autowired
    private RepositoryService repositoryService;

    //通过bpmn部署流程
    @Test
    public void initDeploymentBPMN(){
        String filename = "BPMN/Part4Task.bpmn";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(filename)
                .name("流程部署测试Task")
                .deploy();
        System.out.println(deployment.getName());
    }

    //通过zip部署流程
    @Test
    public void initDeploymentZIP(){
        InputStream fileInputStream = this.getClass().getClassLoader()
                .getResourceAsStream("BPMN/Part1_Deployment.zip");
        ZipInputStream zip = new ZipInputStream(fileInputStream);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zip)
                .name("流程部署ZIP")
                .deploy();
        System.out.println(deployment.getName());
    }

    //查询流程部署
    @Test
    public void getDeployments(){
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for(Deployment dep:list){
            System.out.println("id:"+dep.getId());
            System.out.println("name"+dep.getName());
            System.out.println("time"+dep.getDeploymentTime());
            System.out.println("Key"+dep.getKey());
        }
    }
}
