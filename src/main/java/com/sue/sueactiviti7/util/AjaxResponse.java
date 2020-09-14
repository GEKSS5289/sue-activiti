package com.sue.sueactiviti7.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sue
 * @date 2020/9/14 9:23
 */



@Data
public class AjaxResponse {
    private Integer status;
    private String msg;
    private Object obj;

    private AjaxResponse(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public static AjaxResponse AjaxData(Integer status, String msg, Object obj){
        return new AjaxResponse(status,msg,obj);
    }
}
