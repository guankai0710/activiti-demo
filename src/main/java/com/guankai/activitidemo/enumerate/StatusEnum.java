package com.guankai.activitidemo.enumerate;

/**
 * 类描述：流程状态枚举
 *
 * @author guankai
 * @date 2020/8/25
 **/
public enum StatusEnum {

    HANDLE(1,"待处理"),
    REVIEW(2,"待审核"),
    COMPLETED(3,"已完成");

    /** 状态值 */
    private Integer value;

    /** 状态描述 */
    private String desc;

    StatusEnum(Integer value,String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
