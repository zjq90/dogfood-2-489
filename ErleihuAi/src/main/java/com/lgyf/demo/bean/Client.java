package com.lgyf.demo.bean;

import lombok.Data;

@Data
public class Client {
    private String client_no;
    private String name;
    private String phone;
    private String client_name;
    private String socket_ip;
    private String client_public_key;
    private Integer status;
    private Integer is_agent;
    private String add_time;

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public Integer getIs_agent() {
        return is_agent;
    }
    public void setIs_agent(Integer is_agent) {
        this.is_agent = is_agent;
    }
    public String getClient_no() {
        return client_no;
    }

    public void setClient_no(String client_no) {
        this.client_no = client_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
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

    public String getClient_public_key() {
        return client_public_key;
    }

    public void setClient_public_key(String client_public_key) {
        this.client_public_key = client_public_key;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
