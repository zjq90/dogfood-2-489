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
     * 账户注册功能
     * 功能说明：根据action_code=03进行账户注册，完成二类户开户流程
     * 处理流程：
     * 1、根据身份证号查询数据库判断用户是否已开户，已开户则直接返回开户信息
     * 2、调用ErleihuApi校验家庭地址
     * 3、调用ErleihuApi请求开户
     * 4、开户成功后，添加开户信息到open_account表
     * 5、添加电子账户信息到ele_account表
     * @param args 请求参数Map，包含用户信息和开户所需参数
     * @return 返回处理结果Map，包含响应码、响应信息和开户结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String,String> open_account(@RequestBody Map<String, String> args) {
        StringBuffer log = new StringBuffer();
        log.append("账户注册参数:" + JSON.toJSONString(args) + "\n");
        Map<String, String> mapMeta = new HashMap<String, String>();
        mapMeta.put("uuid", args.get("uuid"));
        mapMeta.put("suuid", suuid);
        String client_no = args.get("client_no");
        String client_public_key = client.getClient_public_key();
        String name = args.get("name");
        String cert_no = args.get("cert_no");
        String cert_order_no = args.get("cert_order_no");
        String home_address = args.get("home_address");
        String account_no = args.get("account_no");
        String mobile = args.get("mobile");
        String occupation = args.get("occupation");
        String otp_order_no = args.get("otp_order_no");
        String business_no = args.get("business_no");
        String otp_code = args.get("otp_code");
        String client_ip = args.get("client_ip");
        String lbs = args.get("lbs");
        String device_id = args.get("device_id");
        try {
            Map<String, Object> existAccount = openAccountDao.getOneMap("getOpenAccountByCertNo", cert_no);
            if (existAccount != null) {
                log.append("身份证号已开户:" + cert_no + "，直接返回开户信息\n");
                mapMeta.put("name", existAccount.get("name") == null ? "" : existAccount.get("name").toString());
                mapMeta.put("cert_no", existAccount.get("cert_no") == null ? "" : existAccount.get("cert_no").toString());
                mapMeta.put("mobile", existAccount.get("mobile") == null ? "" : existAccount.get("mobile").toString());
                mapMeta.put("ele_account_no", existAccount.get("ele_account_no") == null ? "" : existAccount.get("ele_account_no").toString());
                mapMeta.put("agreement_no", existAccount.get("agreement_no") == null ? "" : existAccount.get("agreement_no").toString());
                return TradeDoArgs.return_success(mapMeta, client_no, client_public_key, api_private_key);
            }
            log.append("开始校验家庭地址\n");
            ObpApiIbankAcctCheckUserInfoResponse checkResponse = ErleihuApi.obpApiIbankAcctCheckUserInfo(name, cert_no, home_address, home_address);
            log.append("家庭地址校验结果:" + JSON.toJSONString(checkResponse) + "\n");
            if (checkResponse == null) {
                return TradeDoArgs.return_error(args, "002", "家庭地址校验异常");
            }
            if (!"000000".equals(checkResponse.getResponseCode())) {
                return TradeDoArgs.return_error(args, "006", "家庭地址校验失败:" + checkResponse.getResponseMsg());
            }
            if (!"000000".equals(checkResponse.getBizCode())) {
                return TradeDoArgs.return_error(args, "006", "家庭地址校验失败:" + checkResponse.getBizMsg());
            }
            log.append("开始请求开户\n");
            String thirdId = SnowflakeIdWorker.generateId() + "";
            String openAccountId = SnowflakeIdWorker.generateId() + "";
            ObpApiIbankAcctReturnWorkOpenAccountResponse openAccountResponse = ErleihuApi.obpApiIbankAcctReturnWorkOpenAccount(
                    openAccountId, thirdId, business_no, cert_order_no, otp_order_no, otp_code,
                    name, cert_no, account_no, mobile, "OA001", occupation, client_ip, lbs, device_id, "", "", home_address);
            log.append("开户请求结果:" + JSON.toJSONString(openAccountResponse) + "\n");
            if (openAccountResponse == null) {
                return TradeDoArgs.return_error(args, "010", "开户请求异常");
            }
            if (!"000000".equals(openAccountResponse.getResponseCode())) {
                return TradeDoArgs.return_error(args, "010", "开户失败:" + openAccountResponse.getResponseMsg());
            }
            if (!"000000".equals(openAccountResponse.getBizCode())) {
                return TradeDoArgs.return_error(args, "010", "开户失败:" + openAccountResponse.getBizMsg());
            }
            String ele_account_no = openAccountResponse.getAccountNo();
            String agreement_no = openAccountResponse.getAgreementNo();
            log.append("开户成功，电子账户号:" + ele_account_no + "，协议编号:" + agreement_no + "\n");
            OpenAccount openAccount = new OpenAccount();
            openAccount.setClilent_no(client_no);
            openAccount.setName(name);
            openAccount.setAccount_no(account_no);
            openAccount.setMobile(mobile);
            openAccount.setCert_no(cert_no);
            openAccount.setOtp_order_no(otp_order_no);
            openAccount.setBusiness_no(business_no);
            openAccount.setClient_ip(client_ip);
            openAccount.setHome_address(home_address);
            openAccount.setLbs(lbs);
            openAccount.setDevice_id(device_id);
            openAccount.setEle_account_no(ele_account_no);
            openAccount.setAgreement_no(agreement_no);
            openAccount.setOpen_status("1");
            openAccount.setMsg("开户成功");
            openAccount.setOpen_order_no(openAccountId);
            int insertResult = openAccountDao.insert("ist_open_account", openAccount);
            if (insertResult <= 0) {
                log.append("插入开户记录失败\n");
                throw new RuntimeException("插入开户记录失败");
            }
            log.append("插入开户记录成功\n");
            EleAccount eleAccount = new EleAccount();
            eleAccount.setClilent_no(client_no);
            eleAccount.setName(name);
            eleAccount.setAccount_no(account_no);
            eleAccount.setMobile(mobile);
            eleAccount.setCert_no(cert_no);
            eleAccount.setEle_account_no(ele_account_no);
            eleAccount.setAgreement_no(agreement_no);
            eleAccount.setEle_account_status("1");
            int insertEleResult = openAccountDao.insert("ist_ele_account", eleAccount);
            if (insertEleResult <= 0) {
                log.append("插入电子账户记录失败\n");
                throw new RuntimeException("插入电子账户记录失败");
            }
            log.append("插入电子账户记录成功\n");
            mapMeta.put("name", name);
            mapMeta.put("cert_no", cert_no);
            mapMeta.put("mobile", mobile);
            mapMeta.put("ele_account_no", ele_account_no);
            mapMeta.put("agreement_no", agreement_no);
            return TradeDoArgs.return_success(mapMeta, client_no, client_public_key, api_private_key);
        } catch (Exception e) {
            log.append(suuid + "账户注册异常:" + e.getMessage() + "\n");
            error.error(suuid + "账户注册open_account异常", e);
            return TradeDoArgs.return_error(args, "002", "账户注册异常:" + e.getMessage());
        } finally {
            logger.info(log.toString());
        }
    }


}
