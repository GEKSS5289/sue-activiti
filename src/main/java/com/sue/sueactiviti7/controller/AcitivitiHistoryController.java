package com.sue.sueactiviti7.controller;

import com.sue.sueactiviti7.SecurityUtil;
import com.sue.sueactiviti7.pojo.UserInfo;
import com.sue.sueactiviti7.util.AjaxResponse;
import com.sue.sueactiviti7.util.GlobalConfig;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;

import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author sue
 * @date 2020/9/14 12:48
 */


@RestController
@RequestMapping("/activitHistory")
public class AcitivitiHistoryController {
    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;
    //用户历史任务


    @GetMapping(value = "/getInstancesByUserName")
    public AjaxResponse getInstances(@AuthenticationPrincipal UserInfo userInfo){
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .taskAssignee(userInfo.getUsername())
                    .list();


            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),historicTaskInstances);
        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "获取用户历史任务失败",
                            e.getMessage()
                    );
        }
    }







    //根据流程实例ID查询任务
    @GetMapping(value = "/getInstancesByPiID")
    public AjaxResponse getInstancesByPiID(@RequestParam("piID")String piID){
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .processInstanceId(piID)
                    .list();


            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),historicTaskInstances);
        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "获取历史任务失败",
                            e.getMessage()
                    );
        }
    }











    //根据流程实例ID查询任务
    @GetMapping(value = "/getHighLine")
    public AjaxResponse getHighLine(@RequestParam("instanceID")String instanceID,@AuthenticationPrincipal UserInfo userInfo){
        try {

            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceID).singleResult();
            //读取BPMN
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);
            Collection<FlowElement> flowElements = process.getFlowElements();
            HashMap<String,String> map = new HashMap<>();
            for(FlowElement flowElement:flowElements){
                //判断是否是线条
                if(flowElement instanceof SequenceFlow){
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String ref = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(ref + targetRef,sequenceFlow.getId());
                }
            }

            //获取全部历史流程节点
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processDefinitionId(instanceID)
                    .list();
            //各历史节点两两组合key
            Set<String> keyList = new HashSet<>();
            for(HistoricActivityInstance i : list){
                for(HistoricActivityInstance j:list){
                    if(i != j){
                        keyList.add(i.getActivityId()+j.getActivityId());
                    }
                }
            }


            //高亮连线ID
            Set<String> hightLine = new HashSet<>();
            keyList.forEach(s->hightLine.add(map.get(s)));

            //获取已经完成的节点
            List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                    .processDefinitionId(instanceID)
                    .finished()
                    .list();

            //已经完成的节点高亮
            Set<String> highPoint = new HashSet<>();
            listFinished.forEach(s->highPoint.add(s.getActivityId()));




            //获取待办节点
            List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                    .processDefinitionId(instanceID)
                    .unfinished()
                    .list();

            //待办高亮节点
            Set<String> waitingToDO = new HashSet<>();
            listUnFinished.forEach(s->waitingToDO.add(s.getActivityId()));


            String assigneeName = null;
            if(GlobalConfig.Test){
                assigneeName = "bajie";
            }else{
                assigneeName = userInfo.getUsername();
            }

            //获取待办节点
            List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(assigneeName)
                    .processDefinitionId(instanceID)
                    .finished()
                    .list();




            //当前用户完成的任务
            Set<String> iDo = new HashSet<>();
            taskInstances.forEach(s-> iDo.add(s.getTaskDefinitionKey()));

            HashMap<String,Object> reMap = new HashMap<>();
            reMap.put("highPoint",highPoint);
            reMap.put("hightLine",hightLine);
            reMap.put("waitingToDO",waitingToDO);
            reMap.put("iDo",iDo);








            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),reMap);
        }catch (Exception e){
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "高亮历史任务失败",
                            e.getMessage()
                    );
        }
    }


































}

