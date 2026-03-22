package com.lgyf.demo.bean;

import lombok.Data;

@Data
public class AgentArgs {
    private String agent_no;
    private String public_key;
    private String socket_ip;
    private Integer status;
    private String name;

    public String getAgent_no() {
        return agent_no;
    }

    public void setAgent_no(String agent_no) {
        this.agent_no = agent_no;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getSocket_ip() {
        return socket_ip;
    }

    public void setSocket_ip(String socket_ip) {
        this.socket_ip = socket_ip;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
