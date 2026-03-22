package com.lgyf.demo.service;

import com.alibaba.fastjson.JSON;
import com.lgyf.demo.bean.*;
import com.lgyf.demo.config.TradeDoArgs;
import com.lgyf.demo.dao.ClientDao;
import com.lgyf.demo.dao.OpenAccountDao;
import com.lgyf.demo.pojo.ErleihuApi;
import com.lgyf.demo.util.*;
import com.pab.is.obp.easysdk.client.model.*;
import com.pingan.api.util.FileUploadResponse;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


import javax.annotation.Resource;
import java.util.*;

@SuppressWarnings("all")
@Component
public class ErLeihuDataApi {
    @Autowired
    private RedisCacheService redisCacheService;
    private static String api_private_key=Return.getApi_private_key();
    private static String api_public_key=Return.getApi_public_key();
    private String suuid="";
    private Client client;
    private AgentArgs agentArgs;
    private ErleihuApi erleihuApi = new ErleihuApi();
    private PicUtils picUtils = new PicUtils();
    private static String idcardUrl= ResourceBundle.getBundle("commondata").getString("idcardUpload");
    @Resource
    private ClientDao clientDao;
    @Resource
    private OpenAccountDao openAccountDao;
    @Resource
    private EleAccountDao eleAccountDao;

    private static Logger logger = LoggerFactory.getLogger("info");
    private static Logger error = LoggerFactory.getLogger("error");

    public AgentArgs getAgentArgs() {
        return agentArgs;
    }

    public void setAgentArgs(AgentArgs agentArgs) {
        this.agentArgs = agentArgs;
    }

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

    public Map<String,String> get_client_reg(@RequestBody Map<String, String> args) {
        StringBuffer log=new StringBuffer();
        log.append("商户开通二类户参数:"+JSON.toJSONString(args)+"\n");
        Map<String, String> mapMeta = new HashMap<String, String>();
        mapMeta.put("uuid",args.get("uuid"));
        mapMeta.put("suuid",suuid);
        String client_no  = args.get("client_no");
        String client_public_key=agentArgs.getPublic_key();
        String reg_client_no=args.get("reg_client_no");
        String msg="成功";
        Integer insert=0;
        Client reg_client=redisCacheService.getClient(reg_client_no);
        try{
            if(reg_client!=null){
                 msg="企业已开通二类户,不能重复开通";
                 log.append(msg+"\n");
                  insert=2;//无需插入
                 return TradeDoArgs.return_error(args,"014",msg);
            }
            reg_client=new Client();
            String client_name=args.get("client_name");
            reg_client.setClient_no(reg_client_no);
            reg_client.setClient_name(args.get("short_name"));
            reg_client.setIs_agent(0);
            reg_client.setName(client_name);
            reg_client.setPhone(args.get("mobile"));
            reg_client.setSocket_ip(args.get("socket_ip"));
            reg_client.setStatus(1);
            String public_key=args.get("public_key").replace("+","-").replace("/","_").trim();
            reg_client.setClient_public_key(public_key);
            insert=clientDao.insert("ist_client",reg_client);
            mapMeta.put("reg_client_no",reg_client_no);
            mapMeta.put("client_name",client_name);
            return TradeDoArgs.return_success(mapMeta,client_no,client_public_key,api_private_key);
        } catch (Exception e) {
            log.append(suuid+"企业二类户开通异常\n");
            error.error(suuid+"企业二类户开通get_client_reg",e);
            return TradeDoArgs.return_error(args,"002","企业二类户开通异常");
        }finally {
            if(insert==0){
                log.append("插入数据库失败:"+JSON.toJSONString(reg_client)+"\n");
            }
            logger.info(log.toString());
        }
    }

    /**
     * 账户注册（二类户开户）
     * @param args 请求参数
     * @return 响应结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String,String> openAccount(@RequestBody Map<String, String> args) {
        StringBuffer log = new StringBuffer();
        log.append("[").append(suuid).append("]二类户开户请求参数:").append(JSON.toJSONString(args)).append("\n");
        
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("uuid", args.get("uuid"));
        resultMap.put("suuid", suuid);
        
        String clientNo = args.get("client_no");
        String clientPublicKey = client.getClient_public_key();
        
        // 提取请求参数
        String name = args.get("name");
        String certNo = args.get("cert_no");
        String certOrderNo = args.get("cert_order_no");
        String homeAddress = args.get("home_address");
        String accountNo = args.get("account_no");
        String mobile = args.get("mobile");
        String occupation = args.get("occupation");
        String otpOrderNo = args.get("otp_order_no");
        String businessNo = args.get("business_no");
        String otpCode = args.get("otp_code");
        String clientIp = args.get("client_ip");
        String lbs = args.get("lbs");
        String deviceId = args.get("device_id");
        
        try {
            // 1. 检查是否已开户（优先从电子账户表查询）
            log.append("[").append(suuid).append("]检查是否已开户，身份证号:").append(certNo).append("\n");
            EleAccount existEleAccount = eleAccountDao.getByCertNo(certNo);
            if (existEleAccount != null) {
                log.append("[").append(suuid).append("]该身份证已开户（电子账户表），直接返回信息\n");
                resultMap.put("name", existEleAccount.getName());
                resultMap.put("cert_no", existEleAccount.getCert_no());
                resultMap.put("mobile", existEleAccount.getMobile());
                resultMap.put("ele_account_no", existEleAccount.getEle_account_no());
                resultMap.put("agreement_no", existEleAccount.getAgreement_no() == null ? "" : existEleAccount.getAgreement_no());
                resultMap.put("action_code", "03");
                return TradeDoArgs.return_success(resultMap, clientNo, clientPublicKey, api_private_key);
            }
            
            // 从开户记录表查询
            OpenAccount existAccount = openAccountDao.getByCertNo(certNo);
            if (existAccount != null && "1".equals(existAccount.getOpen_status())) {
                log.append("[").append(suuid).append("]该身份证已开户（开户记录表），直接返回信息\n");
                resultMap.put("name", existAccount.getName());
                resultMap.put("cert_no", existAccount.getCert_no());
                resultMap.put("mobile", existAccount.getMobile());
                resultMap.put("ele_account_no", existAccount.getEle_account_no());
                resultMap.put("agreement_no", existAccount.getAgreement_no() == null ? "" : existAccount.getAgreement_no());
                resultMap.put("action_code", "03");
                return TradeDoArgs.return_success(resultMap, clientNo, clientPublicKey, api_private_key);
            }
            
            // 2. 家庭地址校验（调用银行接口）
            log.append("[").append(suuid).append("]开始家庭地址校验\n");
            ObpApiIbankAcctCheckUserInfoResponse checkResult = ErleihuApi.obpApiIbankAcctCheckUserInfo(
                name, certNo, homeAddress, homeAddress);
            log.append("[").append(suuid).append("]家庭地址校验结果:").append(JSON.toJSONString(checkResult)).append("\n");
            
            if (checkResult == null) {
                log.append("[").append(suuid).append("]家庭地址校验异常\n");
                return TradeDoArgs.return_error(args, "002", "家庭地址校验异常");
            }
            
            if (!"000000".equals(checkResult.getResponseCode()) || !"000000".equals(checkResult.getBizCode())) {
                String errMsg = checkResult.getResponseMsg() == null ? checkResult.getBizMsg() : checkResult.getResponseMsg();
                log.append("[").append(suuid).append("]家庭地址校验失败:").append(errMsg).append("\n");
                return TradeDoArgs.return_error(args, "006", "家庭地址校验失败:" + errMsg);
            }
            
            // 3. 保存初始开户记录
            log.append("[").append(suuid).append("]保存初始开户记录\n");
            OpenAccount openAccount = new OpenAccount();
            openAccount.setClilent_no(clientNo);
            openAccount.setName(name);
            openAccount.setAccount_no(accountNo);
            openAccount.setMobile(mobile);
            openAccount.setOpen_status("0"); // 0:开户中, 1:开户成功, 2:开户失败
            openAccount.setCert_no(certNo);
            openAccount.setOtp_order_no(otpOrderNo);
            openAccount.setBusiness_no(businessNo);
            openAccount.setClient_ip(clientIp);
            openAccount.setHome_address(homeAddress);
            openAccount.setLbs(lbs);
            openAccount.setDevice_id(deviceId);
            openAccount.setMsg("开户中");
            openAccount.setOpen_order_no(businessNo);
            
            int insertResult = openAccountDao.insertOpenAccount(openAccount);
            if (insertResult <= 0) {
                log.append("[").append(suuid).append("]开户记录插入失败\n");
                return TradeDoArgs.return_error(args, "002", "开户记录保存失败");
            }
            
            // 4. 调用银行开户接口
            log.append("[").append(suuid).append("]调用银行开户接口\n");
            String openAccountId = UUID.randomUUID().toString().replace("-", "");
            String thirdId = clientNo + "_" + certNo;
            String scene = "OA002"; // 开户场景
            String fullDeviceNumber = ""; // SIM卡号（可选）
            String appName = "ErleihuAi";
            
            ObpApiIbankAcctReturnWorkOpenAccountResponse openResult = ErleihuApi.obpApiIbankAcctReturnWorkOpenAccount(
                openAccountId, thirdId, businessNo, certOrderNo, otpOrderNo, otpCode,
                name, certNo, accountNo, mobile, scene, occupation, clientIp, lbs,
                deviceId, fullDeviceNumber, appName, homeAddress);
            
            log.append("[").append(suuid).append("]银行开户接口返回:").append(JSON.toJSONString(openResult)).append("\n");
            
            if (openResult == null) {
                openAccount.setOpen_status("2");
                openAccount.setMsg("开户接口调用失败");
                openAccountDao.updateOpenStatus(openAccount);
                return TradeDoArgs.return_error(args, "010", "开户接口调用失败");
            }
            
            if (!"000000".equals(openResult.getResponseCode()) || !"000000".equals(openResult.getBizCode())) {
                String errMsg = openResult.getResponseMsg() == null ? 
                    (openResult.getBizMsg() == null ? "开户失败" : openResult.getBizMsg()) : 
                    openResult.getResponseMsg();
                openAccount.setOpen_status("2");
                openAccount.setMsg("开户失败:" + errMsg);
                openAccountDao.updateOpenStatus(openAccount);
                return TradeDoArgs.return_error(args, "010", "开户失败:" + errMsg);
            }
            
            // 5. 第一步：更新开户记录状态为成功
            log.append("[").append(suuid).append("]开户成功，第一步：更新开户记录状态\n");
            openAccount.setOpen_status("1");
            openAccount.setEle_account_no(openResult.getAccountNo());
            openAccount.setAgreement_no(openResult.getAgreementNo());
            openAccount.setMsg("开户成功");
            int updateResult = openAccountDao.updateOpenStatus(openAccount);
            if (updateResult <= 0) {
                log.append("[").append(suuid).append("]开户记录更新失败\n");
                return TradeDoArgs.return_error(args, "002", "开户记录更新失败");
            }
            
            // 6. 第二步：保存电子账户信息
            log.append("[").append(suuid).append("]第二步：保存电子账户信息\n");
            EleAccount eleAccount = new EleAccount();
            eleAccount.setClilent_no(clientNo);
            eleAccount.setName(name);
            eleAccount.setAccount_no(accountNo);
            eleAccount.setMobile(mobile);
            eleAccount.setCert_no(certNo);
            eleAccount.setEle_account_no(openResult.getAccountNo());
            eleAccount.setAgreement_no(openResult.getAgreementNo());
            eleAccount.setEle_account_status("1"); // 1:有效
            
            int eleInsertResult = eleAccountDao.insertEleAccount(eleAccount);
            if (eleInsertResult <= 0) {
                log.append("[").append(suuid).append("]电子账户信息保存失败\n");
                // 注意：此处如果电子账户信息保存失败，是否需要回滚开户记录？
                // 根据业务需求，如果需要强一致性，可以在这里抛出异常触发回滚
                // 或者记录错误日志继续执行
                throw new RuntimeException("电子账户信息保存失败");
            }
            
            // 7. 组装返回结果
            resultMap.put("name", name);
            resultMap.put("cert_no", certNo);
            resultMap.put("mobile", mobile);
            resultMap.put("ele_account_no", openResult.getAccountNo());
            resultMap.put("agreement_no", openResult.getAgreementNo() == null ? "" : openResult.getAgreementNo());
            resultMap.put("action_code", "03");
            
            log.append("[").append(suuid).append("]开户成功，电子账号:").append(openResult.getAccountNo()).append("\n");
            return TradeDoArgs.return_success(resultMap, clientNo, clientPublicKey, api_private_key, "开户成功");
            
        } catch (Exception e) {
            log.append("[").append(suuid).append("]开户异常:").append(e.getMessage()).append("\n");
            error.error("[" + suuid + "]二类户开户异常", e);
            return TradeDoArgs.return_error(args, "002", "系统异常:" + e.getMessage());
        } finally {
            logger.info(log.toString());
        }
    }
}

