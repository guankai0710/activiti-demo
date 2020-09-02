package com.guankai.activitidemo.vo;

/**
 * 类描述：流程定义信息VO
 *
 * @author guankai
 * @date 2020/8/19
 **/
public class ProcessDefinitionVo {

    /** 流程定义id */
    private String id;

    /** 流程名称 */
    private String name;

    /** 流程key */
    private String key;

    /** 流程文件名 */
    private String resourceName;

    /** 版本号 */
    private Integer version;

    /** 开始节点表单 */
    private String startFormKey;

    public ProcessDefinitionVo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStartFormKey() {
        return startFormKey;
    }

    public void setStartFormKey(String startFormKey) {
        this.startFormKey = startFormKey;
    }
}
