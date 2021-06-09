package com.jhxstudio.jarvis.common.enums;

/**
 * @author jhx
 * @date 2021/6/8
 **/

public enum RCodeEnum {

    OK("0000", "成功"),
    ERROR("500", "系统出现未知异常,请稍后重试"),;

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    RCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
