package com.sue.sueactiviti7.controller;

import com.sue.sueactiviti7.util.AjaxResponse;
import com.sue.sueactiviti7.util.GlobalConfig;
import org.activiti.engine.RepositoryService;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

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


    //获取流程部署列表




    //删除流程定义
    @PostMapping(value = "/delDefinition")
    public AjaxResponse delDefinition(
            @RequestParam("pdID")String pdID){

        try{

            repositoryService.deleteDeployment(pdID,true);



            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            null
                    );


        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "删除流程定义失败",
                            e.getMessage()
                    );
        }


    }


































    @GetMapping(value = "/getDeployments")
    public AjaxResponse getDeployments(){
        try {

            List<HashMap<String,Object>> listmap = new ArrayList<HashMap<String, Object>>();
            List<Deployment> list = repositoryService.createDeploymentQuery().list();


            for(Deployment dep:list){
                HashMap<String,Object> hashMap = new HashMap<>();


                hashMap.put("Id",dep.getId());
                hashMap.put("Name",dep.getName());
                hashMap.put("DeploymentTime",dep.getDeploymentTime());



                listmap.add(hashMap);
            }


            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),null
                    );


        }catch (Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"获取流程部署列表失败",e.toString());
        }

    }



















    //获取流程定义XML
    @GetMapping("/getDefinitionXML")
    public void getDefinitionXML(
            HttpServletResponse response,
            @RequestParam("deploymentID")String deploymentID,
            @RequestParam("resourceName")String resourceName
    ){

        try{


            InputStream inputStream = repositoryService.getResourceAsStream(deploymentID,resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1){
                outputStream.write(bytes);
            }
            inputStream.close();





        }catch (Exception e){
            e.toString();
        }


    }














    //添加流程定义通过在线提交BPMN的xml
    @PostMapping(value = "/addDeploymentByString")
    public AjaxResponse addDeploymentByString(
            @RequestParam("stringBPMN")String stringBPMN
    ){

        try{


            Deployment deployment = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn",stringBPMN)
                    .name("sfsaf")
                    .deploy();




            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            deployment.getId()
                    );


        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "String部署流程失败",
                            e.getMessage()
                    );
        }


    }



















    //添加流程定义通过上传bpmn
    @PostMapping(value = "/uploadStreamAndDeployment")
    public AjaxResponse uploadStreamAndDeployment(
            @RequestParam("processFile")MultipartFile multipartFile,
            @RequestParam("processName")String processName){

        try{
            //获取上传文件名
            String fileName = multipartFile.getOriginalFilename();
            //获取文件扩展名
            String extension = FilenameUtils.getExtension(fileName);
            //获取文件字节流对象
            InputStream fileInputStream = multipartFile.getInputStream();

            Deployment deployment = null;

            if(extension.equals("zip")){

                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()
                .addZipInputStream(zip)
                .name(processName)
                .deploy();

            }else{

                deployment = repositoryService.createDeployment()
                        .addInputStream(fileName,fileInputStream)
                        .name(processName)
                        .deploy();

            }

            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            deployment.getId()+";"+fileName
                    );


        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                           "部署流程失败",
                            e.getMessage()
                    );
        }


    }

















    //获取流程定义列表
    @GetMapping(value = "/getDefinitions")
    public AjaxResponse getDefinitions(){
        try {

            List<HashMap<String,Object>> listmap = new ArrayList<HashMap<String, Object>>();
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();


            for(ProcessDefinition pd:list){
                HashMap<String,Object> hashMap = new HashMap<>();


                hashMap.put("name",pd.getName());
                hashMap.put("key",pd.getKey());
                hashMap.put("resourceName",pd.getResourceName());
                hashMap.put("deploymentId",pd.getDeploymentId());
                hashMap.put("version",pd.getVersion());


                listmap.add(hashMap);
            }


            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),listmap
                    );


        }catch (Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"获取流程定义失败",e.toString());
        }

    }

}
