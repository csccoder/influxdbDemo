package com.csccoder.influxdb.entity;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.util.Date;

@Measurement(name = "logInfo")
public class LogInfo {


    // 注解中添加tag = true,表示当前字段内容为tag内容
    @Column(name = "module", tag = true)
    private String module;
    @Column(name = "level", tag = true)
    private String level;
    @Column(name = "device_id", tag = true)
    private String deviceId;
    @Column(name = "msg")
    private String msg;
    @Column(name = "msg")
    private Integer seq;




    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

}