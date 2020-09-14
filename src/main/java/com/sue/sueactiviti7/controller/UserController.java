package com.sue.sueactiviti7.controller;

import com.sue.sueactiviti7.mapper.ActivitiMapper;
import com.sue.sueactiviti7.util.AjaxResponse;
import com.sue.sueactiviti7.util.GlobalConfig;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author sue
 * @date 2020/9/14 16:54
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ActivitiMapper activitiMapper;

    @GetMapping("/getUsers")
    public AjaxResponse completeTask (@RequestParam("taskID")String taskID){
        try {


            List<HashMap<String,Object>> userList = activitiMapper.selectUser();





            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.SUCCESS.getCode(),
                            GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                            userList
                    );


        } catch (Exception e) {
            return AjaxResponse
                    .AjaxData(
                            GlobalConfig.ResponseCode.ERROR.getCode(),
                            "获取用户列表失败",
                            e.getMessage()
                    );
        }
    }


}
