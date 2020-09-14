package com.sue.sueactiviti7.controller;

import com.sue.sueactiviti7.util.AjaxResponse;
import com.sue.sueactiviti7.util.GlobalConfig;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author sue
 * @date 2020/9/14 9:41
 */

@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    //添加流程定义通过上传bpmn
    //添加流程定义通过在线提交BPMN的xml
    //获取流程定义列表
    //获取流程定义XML

    //获取流程部署列表
    //删除流程定义


    //添加流程定义通过上传bpmn

















    //获取流程定义列表
    @GetMapping(value = "/getDefinitions")
    public AjaxResponse getDefinitions(){
        try {

            List<HashMap<String,Object>> listmap = new ArrayList<HashMap<String, Object>>();
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();


            for(ProcessDefinition pd:list){
                HashMap<String,Object> hashMap = new HashMap<>();


                hashMap.put("Name",pd.getName());
                hashMap.put("Key",pd.getKey());
                hashMap.put("ResourceName",pd.getResourceName());
                hashMap.put("DeploymentId",pd.getDeploymentId());
                hashMap.put("Version",pd.getVersion());


                listmap.add(hashMap);
            }


            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),list.toString()
                    );




        }catch (Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"获取流程定义失败",e.toString());
        }

    }

}
