package com.sue.sueactiviti7.util;

import lombok.Getter;

/**
 * @author sue
 * @date 2020/9/14 9:18
 */


public class GlobalConfig {


    public static final Boolean Test = true;


    @Getter
    public enum ResponseCode{
        SUCCESS(0,"成功"),
        ERROR(1,"错误")

        ;

        private final int code;
        private final String desc;

        ResponseCode(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

}
