package com.lgyf.demo.service;

import com.alibaba.fastjson.JSON;
import com.lgyf.demo.bean.Client;
import com.lgyf.demo.bean.EleAccountPay;
import com.lgyf.demo.bean.ReturnModel;
import com.lgyf.demo.bean.SendSms;
import com.lgyf.demo.config.TradeDoArgs;
import com.lgyf.demo.dao.ClientDao;
import com.lgyf.demo.pojo.RechargeApi;
import com.lgyf.demo.util.DateUtil;
import com.lgyf.demo.util.JsonUtils;
import com.lgyf.demo.util.Return;
import com.pab.is.obp.easysdk.client.model.ObpApiIbankAcctQueryBankTransferLimitSmartResponse;
import com.pab.is.obp.easysdk.client.model.ObpApiIbankAcctToTransferResponse;
import com.pab.is.obp.easysdk.client.model.ObpApiIbankAcctToTransferResultResponse;
import com.pab.is.obp.easysdk.client.model.ObpApiIbankCommonSendOtpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@SuppressWarnings("all")
@Scope("prototype")
@Service
public class ErleihuRechargeApi {
    @Autowired
    private RedisCacheService redisCacheService;
    private static String api_private_key= Return.getApi_private_key();
    private static String api_public_key=Return.getApi_public_key();
    @Autowired
    private RechargeApi rechargeApi;
    private String suuid="";
    private Client client;
    @Resource
    private ClientDao clientDao;

    private static Logger logger = LoggerFactory.getLogger("info");
    private static Logger error = LoggerFactory.getLogger("error");
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getSuuid() {
        return suuid;
    }
    public void setSuuid(String suuid) {
        this.suuid = suuid;
    }
    //12 零售渠道专用II类户充值提现.otp发送接口 youversion[出金转账]
    public Map<String, String> get_api_sendOtpChuJing(@RequestBody Map<String, String> args) {
        Map<String, String> mapMeta = new HashMap<String, String>();
        StringBuffer log=new StringBuffer();
        mapMeta.put("uuid",args.get("uuid"));
        mapMeta.put("suuid",suuid);
        String client_no  = args.get("client_no");
        String client_public_key=client.getClient_public_key();
        String businessNo = "OB"+ ResourceBundle.getBundle("pabObpClient").getString("pab.obp.appId")+"00"+new DateUtil().getDateFormate(new Date(),"YYYYMMdd")+"00"+(int)(1+Math.random()*(100000000-1+1))+"";
        String scene  = "BC001";
        String accountNo  = args.get("ele_account_no");
        String mobileNo  = args.get("mobile");
        String amount  = args.get("amt");
        String fromAccountNo  = args.get("ele_account_no");
        String fromAccountName  = args.get("name");
        String toAccountNo  = args.get("to_account_no");
        String accountName  = args.get("to_name");
        String msg="成功";
        String sms_status="0";
        String otp_order_no="";
        log.append("提现短信流水号"+businessNo+"参数:"+ JSON.toJSONString(args));
        try{
            ObpApiIbankCommonSendOtpResponse obpApiIbankCommonSendOtpResponse = rechargeApi.obpApiIbankCommonSendOtpChuJing(businessNo, scene, accountName, fromAccountName, toAccountNo, fromAccountNo, amount, accountNo, mobileNo);
            if(obpApiIbankCommonSendOtpResponse.getResponseCode().equals("000000")) {
                if(!obpApiIbankCommonSendOtpResponse.getStatus().equals("0")){
                     sms_status="1";
                     msg="转账短信发送失败"+obpApiIbankCommonSendOtpResponse.getBizCode()+":"+obpApiIbankCommonSendOtpResponse.getBizMsg();
                     return TradeDoArgs.return_error(args,"009",msg);
                }
                otp_order_no=obpApiIbankCommonSendOtpResponse.getOtpOrderNo();
                mapMeta.put("amt", amount);
                mapMeta.put("to_account_no", toAccountNo);
                mapMeta.put("to_name", accountName);
                mapMeta.put("otp_order_no", otp_order_no);
                mapMeta.put("business_no",businessNo);
                return TradeDoArgs.return_success(mapMeta,client_no,client_public_key,api_private_key);
            }else{
                msg="转账短信发送失败"+obpApiIbankCommonSendOtpResponse.getResponseCode()+":"+obpApiIbankCommonSendOtpResponse.getResponseMsg();
                return TradeDoArgs.return_error(args,"009",msg);
            }
        } catch (Exception e) {
              msg="转账短信发送异常失败";
              error.error(suuid+msg,e);
              return TradeDoArgs.return_error(args,"009",msg);
        }finally {
              log.append(msg+"\n");
              logger.info(log.toString());
              SendSms sendSms=new SendSms();
              sendSms.setMsg(msg+"["+toAccountNo+"-"+accountName+"-"+amount+"]");
              sendSms.setSms_status(sms_status);
              sendSms.setScece(scene);
              sendSms.setOtp_order_no(otp_order_no);
              sendSms.setName(fromAccountName);
              sendSms.setAccount_no(fromAccountNo);
              sendSms.setMobile(mobileNo);
              sendSms.setBusiness_no(businessNo);
              sendSms.setClilent_no(client_no);
              sendSms.setCert_no("0");
              int insert=0;
              try {
                  insert = clientDao.insert("send_sms", sendSms);
              }catch (Exception e){
                  insert=0;
                  error.error(suuid+"保存短信数据异常失败",e);
              }finally {
                  logger.info(suuid+"保存短信数据结果："+(insert==0?"失败":"成功")+":"+JSON.toJSONString(sendSms));
              }
        }
    }
    //13 零售渠道专用II类户充值提现.出金转账接口
    public  Map<String, String> get_api_acctToTransfer(@RequestBody Map<String, String> args) {
        Map<String, String> mapMeta = new HashMap<String, String>();
        StringBuffer log=new StringBuffer();
        log.append(suuid+"出款参数："+JSON.toJSONString(args)+"\n");
        mapMeta.put("uuid",args.get("uuid"));
        mapMeta.put("suuid",suuid);
        String client_no  = args.get("client_no");
        String client_public_key=client.getClient_public_key();
        String thirdId  = args.get("cert_no");
        String businessNo  = args.get("business_no");
        String amount  = args.get("amt");
        String fromAccountNo  = args.get("ele_account_no");
        String fromAccountName  = args.get("name");
        String toAccountNo  = args.get("to_account_no");
        String accountName  = args.get("to_name");
        String bussinessScence  ="c371768950";
        String otpOrderNo  = args.get("otp_order_no");
        String otpCode  = args.get("otp_code");
        String ipAddr  = args.get("client_ip");
        String remark=args.get("remark");
        String pay_status="0";
        String msg="成功";
        String amt_order_no="";
        String finish_time="";
        try{
            ObpApiIbankAcctToTransferResponse obpApiIbankAcctToTransferResponse = rechargeApi.obpApiIbankAcctToTransfer(businessNo, otpCode, otpOrderNo, thirdId, toAccountNo, accountName, bussinessScence, amount, fromAccountNo, fromAccountName, ipAddr,remark);
            if(!obpApiIbankAcctToTransferResponse.getResponseCode().equals("000000")){
                 msg="转账（提现失败）"+obpApiIbankAcctToTransferResponse.getResponseCode()+":"+obpApiIbankAcctToTransferResponse.getResponseMsg()+"\n";
                 return TradeDoArgs.return_error(args,"012",msg);
            }
            if (!obpApiIbankAcctToTransferResponse.getBizCode().equals("000000")) {
                msg="转账（提现失败）"+obpApiIbankAcctToTransferResponse.getBizCode()+":"+obpApiIbankAcctToTransferResponse.getBizMsg()+"\n";
                return TradeDoArgs.return_error(args,"012",msg);
            }
            finish_time=obpApiIbankAcctToTransferResponse.getTransTime();
            msg=obpApiIbankAcctToTransferResponse.getBizMsg();
            pay_status=obpApiIbankAcctToTransferResponse.getStatus();
            amt_order_no=obpApiIbankAcctToTransferResponse.getOrderSerialNo();
            mapMeta.put("name", fromAccountName);
            mapMeta.put("ele_account_no", fromAccountNo);
            mapMeta.put("to_account", toAccountNo);
            mapMeta.put("to_name", accountName);
            mapMeta.put("msg", obpApiIbankAcctToTransferResponse.getMsg());
            mapMeta.put("status",pay_status);
            mapMeta.put("amt_order_no",amt_order_no);
            mapMeta.put("time", finish_time);
            mapMeta.put("amt",amount);
            mapMeta.put("business_no", businessNo);
            mapMeta.put("remark", remark);
            mapMeta.put("msg",msg);
            return TradeDoArgs.return_success(mapMeta,client_no,client_public_key,api_private_key);
        } catch (Exception e) {
            msg="提现转账异常失败";
            error.error(suuid+msg,e);
            return TradeDoArgs.return_error(args,"012",msg);
        }finally {
            log.append(msg+"\n");
            logger.info(log.toString());
            EleAccountPay eleAccountPay=new EleAccountPay();
            eleAccountPay.setClilent_no(client_no);
            eleAccountPay.setAmt_order_no(amt_order_no);
            eleAccountPay.setAmt(amount);
            eleAccountPay.setBusiness_no(businessNo);
            eleAccountPay.setCert_no(thirdId);
            eleAccountPay.setClient_ip(ipAddr);
            eleAccountPay.setEle_account_no(fromAccountNo);
            eleAccountPay.setName(fromAccountName);
            eleAccountPay.setTo_account_no(toAccountNo);
            eleAccountPay.setTo_name(accountName);
            eleAccountPay.setFinish_time(finish_time);
            eleAccountPay.setMsg(msg);
            eleAccountPay.setOtp_order_no(otpOrderNo);
            eleAccountPay.setPay_status(pay_status);
            eleAccountPay.setRemark(remark);
            int insert=0;
            try {
                insert=clientDao.insert("ele_account_pay", eleAccountPay);
            }catch (Exception e){
                error.error(suuid+"保存转账数据失败",e);
            }finally {
                logger.info(suuid+"保存转账数据结果："+(insert==0?"失败":"成功")+":"+JSON.toJSONString(eleAccountPay));
            }
        }
    }
    //14 零售渠道专用II类户充值提现.出金转账结果查询
    public  Map<String, String> get_api_acctToTransferResult(@RequestBody Map<String, String> args) {
        Map<String, String> mapMeta = new HashMap<String, String>();
        mapMeta.put("uuid",args.get("uuid"));
        mapMeta.put("suuid",suuid);
        String client_no  = args.get("client_no");
        String client_public_key=client.getClient_public_key();
        try{
            String thirdId  = args.get("cert_no");
//            String bno  = args.get("businessNo");
            String accountNo  = args.get("ele_account_no");
            String orderSerialNo  = args.containsKey("amt_order_no")?args.get("amt_order_no"):"";
            String business_no  = args.containsKey("business_no")?args.get("business_no"):"";
            if(orderSerialNo.equals("")&&business_no.equals("")){
                   return TradeDoArgs.return_error(args,"005","参数-银行指令号、流水号必填其一");
            }
            ObpApiIbankAcctToTransferResultResponse obpApiIbankAcctToTransferResultResponse = rechargeApi.obpApiIbankAcctToTransferResult(business_no,orderSerialNo, thirdId, accountNo);
            if(obpApiIbankAcctToTransferResultResponse.getResponseCode().equals("000000")) {
                if (obpApiIbankAcctToTransferResultResponse.getBizCode().equals("000000")) {
                    mapMeta.put("cert_no",thirdId);
                    mapMeta.put("ele_account_no",accountNo);
                    mapMeta.put("amt_order_no",orderSerialNo);
                    mapMeta.put("business_no",business_no);
                    mapMeta.put("status", obpApiIbankAcctToTransferResultResponse.getStatus());
                    mapMeta.put("time", obpApiIbankAcctToTransferResultResponse.getTransTime());
                    mapMeta.put("msg", obpApiIbankAcctToTransferResultResponse.getBizMsg());
                    return TradeDoArgs.return_success(mapMeta,client_no,client_public_key,api_private_key);
                } else {
                    return TradeDoArgs.return_error(args,"013","查询转账失败"+obpApiIbankAcctToTransferResultResponse.getBizCode()+":"+obpApiIbankAcctToTransferResultResponse.getBizMsg());

                }
            }else{
                return TradeDoArgs.return_error(args,"013","查询转账失败"+obpApiIbankAcctToTransferResultResponse.getResponseCode()+":"+obpApiIbankAcctToTransferResultResponse.getResponseMsg());

            }
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.error(methodName + "=========" + e);
            return TradeDoArgs.return_error(args,"013","查询转账异常失败");
        }
    }
    //15 零售渠道专用II类户充值提现.代扣银行卡限额查询
    public Map<String, String> get_api_AcctQueryBankTransferLimitSmart(@RequestBody Map<String, String> args) {
        Map<String, String> mapMeta = new HashMap<String, String>();
        String checkArgsString="businessNo,cardNos";
        String checkmsg=Return.checkArgNull(checkArgsString,args);
        if(!checkmsg.equals("")){
            return Return.returnError(args,"400000",checkmsg);
        }
        String client_no  = args.get("client_no");
        Client client=redisCacheService.getClient(client_no);
        String client_public_key=client.getClient_public_key();
        try{
            String businessNo  = args.get("businessNo");
            String cardNos  = args.get("cardNos");

            ObpApiIbankAcctQueryBankTransferLimitSmartResponse obpApiIbankAcctQueryBankTransferLimitSmartResponse = rechargeApi.obpApiIbankAcctQueryBankTransferLimitSmart(businessNo, cardNos);
            if(obpApiIbankAcctQueryBankTransferLimitSmartResponse.getResponseCode().equals("000000")) {
                if (obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBizCode().equals("000000")) {
                    mapMeta.put("bankCode", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBankCode());
                    mapMeta.put("bankId", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBankId());
                    mapMeta.put("bankName", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBankName());
                    mapMeta.put("canPay", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getCanPay());
                    mapMeta.put("cardBin", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getCardBin());
                    mapMeta.put("cardType", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getCardType());
                    mapMeta.put("dayLimit", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getDayLimit());
                    mapMeta.put("oneLimit", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getOneLimit());
                    mapMeta.put("bibOpenBankCode", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBibOpenBankCode());
                    mapMeta.put("msg", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBizMsg());
                    mapMeta.put("status", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBizCode());
                    return Return.returnSuccess(mapMeta,client_no,client_public_key,api_private_key);
                } else {
                    mapMeta.put("msg", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBizMsg());
                    mapMeta.put("status", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getBizCode());
                    return Return.returnSuccess(mapMeta,client_no,client_public_key,api_private_key);
                }
            }else{
                mapMeta.put("msg", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getResponseMsg());
                mapMeta.put("status", obpApiIbankAcctQueryBankTransferLimitSmartResponse.getResponseCode());
                return Return.returnSuccess(mapMeta,client_no,client_public_key,api_private_key);
            }
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.error(methodName + "=========" + e);
            mapMeta.put("msg", "失败");
            mapMeta.put("status", "900000");
            return Return.returnSuccess(mapMeta,client_no,client_public_key,api_private_key);
        }
    }
}
