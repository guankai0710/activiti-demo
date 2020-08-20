package com.guankai.activitidemo.vo;

import java.io.Serializable;

/**
 * 自定义控制器返回模型
 *
 * @author: guan.kai
 * @date: 2020/8/18
 **/
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 1320619120873828769L;

    /** 状态 */
    boolean success = false;

    /** 信息 */
    String msg;

    /** 数据 */
    transient Object obj;

    public JsonResult() {
    }

    public JsonResult(boolean success, String msg, Object obj) {
        this.success = success;
        this.msg = msg;
        this.obj = obj;
    }

    public static JsonResult sucess(){
        return sucess(null);
    }

    public static JsonResult sucess(Object obj){
        return sucess(null, obj);
    }

    public static JsonResult sucess(String msg, Object obj){
        return new JsonResult(true, msg, obj);
    }

    public static JsonResult failure(){
        return failure(null);
    }

    public static JsonResult failure(String msg){
        return failure(msg, null);
    }

    public static JsonResult failure(String msg, Object obj){
        return new JsonResult(false, msg, obj);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
