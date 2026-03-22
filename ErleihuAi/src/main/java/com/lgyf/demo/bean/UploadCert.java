package com.lgyf.demo.bean;

import lombok.Data;

/***********************************************
 * @Author merry$
 * @Description //TODO 身份证上传记录
 * @Date $ $
 * @Param $
 * @return $
 ***********************************************/
@Data
public class UploadCert {
    private String cert_no;
    private String name;
    private String upload_status;
    private String cert_order_no;
    private String add_time;
    private String msg;
    private String client_no;

    public String getClient_no() {
        return client_no;
    }

    public void setClient_no(String client_no) {
        this.client_no = client_no;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpload_status() {
        return upload_status;
    }

    public void setUpload_status(String upload_status) {
        this.upload_status = upload_status;
    }

    public String getCert_order_no() {
        return cert_order_no;
    }

    public void setCert_order_no(String cert_order_no) {
        this.cert_order_no = cert_order_no;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
