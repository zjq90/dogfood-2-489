package com.lgyf.demo.bean;

/***********************************************    
 * @Author merry$
 * @Description //TODO 电子账户绑卡记录
 * @Date $ $
 * @Param $
 * @return $
 ***********************************************/
public class EleAccountBkcard {
    private String client_no;
    private String bkcard_num;
    private String cert_no;
    private String ele_account_no;
    private String bind_time;
    private String unbind_time;
    private String bind_status;

    public String getClient_no() {
        return client_no;
    }

    public void setClient_no(String client_no) {
        this.client_no = client_no;
    }

    public String getBkcard_num() {
        return bkcard_num;
    }

    public void setBkcard_num(String bkcard_num) {
        this.bkcard_num = bkcard_num;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getEle_account_no() {
        return ele_account_no;
    }

    public void setEle_account_no(String ele_account_no) {
        this.ele_account_no = ele_account_no;
    }

    public String getBind_time() {
        return bind_time;
    }

    public void setBind_time(String bind_time) {
        this.bind_time = bind_time;
    }

    public String getUnbind_time() {
        return unbind_time;
    }

    public void setUnbind_time(String unbind_time) {
        this.unbind_time = unbind_time;
    }

    public String getBind_status() {
        return bind_status;
    }

    public void setBind_status(String bind_status) {
        this.bind_status = bind_status;
    }
}
