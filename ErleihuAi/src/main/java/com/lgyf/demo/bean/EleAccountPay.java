package com.lgyf.demo.bean;

/***********************************************    
 * @Author merry$
 * @Description //TODO 电子账户出款
 * @Date $ $
 * @Param $
 * @return $
 ***********************************************/
public class EleAccountPay {
    private String ele_account_no;
    private String clilent_no;
    private String name;
    private String cert_no;
    private String otp_order_no;
    private String business_no;
    private String client_ip;
    private String msg;
    private String remark;
    private String to_name;
    private String to_account_no;
    private String add_date;
    private String add_time;
    private String amt_order_no;
    private String amt;
    private String pay_status;
    private String finish_time;

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getEle_account_no() {
        return ele_account_no;
    }

    public void setEle_account_no(String ele_account_no) {
        this.ele_account_no = ele_account_no;
    }

    public String getClilent_no() {
        return clilent_no;
    }

    public void setClilent_no(String clilent_no) {
        this.clilent_no = clilent_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getOtp_order_no() {
        return otp_order_no;
    }

    public void setOtp_order_no(String otp_order_no) {
        this.otp_order_no = otp_order_no;
    }

    public String getBusiness_no() {
        return business_no;
    }

    public void setBusiness_no(String business_no) {
        this.business_no = business_no;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_account_no() {
        return to_account_no;
    }

    public void setTo_account_no(String to_account_no) {
        this.to_account_no = to_account_no;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAmt_order_no() {
        return amt_order_no;
    }

    public void setAmt_order_no(String amt_order_no) {
        this.amt_order_no = amt_order_no;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }
}
