
package com.lgyf.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lgyf.demo.bean.AgentArgs;
import com.lgyf.demo.bean.Client;
import com.lgyf.demo.bean.ReturnModel;
import com.lgyf.demo.config.TradeDoArgs;
import com.lgyf.demo.dao.ClientDao;
import com.lgyf.demo.service.ErLeihuDataApi;
import com.lgyf.demo.service.ErleihuRechargeApi;
import com.lgyf.demo.service.RedisCacheService;
import com.lgyf.demo.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * ********************************************************
 *
 * @author 自动生成
 * @ClassName: TradSerialController
 * @Description: 交易流水表
 * @date 2021-01-24 上午 11:59:41
 * ******************************************************
 */
@SuppressWarnings("all")
@Scope("prototype")
@RestController
@RequestMapping("/Erleihu")
public class TradDoController extends PublicController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TradDoController.class);
    private static String api_private_key=Return.getApi_private_key();
    private static String api_public_key=Return.getApi_public_key();

    @Autowired
    private PicUtils picUtils;
    @Autowired
    private ErLeihuDataApi erLeihuDataApi;
    @Autowired
    private ErleihuRechargeApi erleihuRechargeApi;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private RedisCacheService redisCacheService;

    private Client client;
    private AgentArgs agentArgs;
    private String suuid;
//"OA001：代表开户 OA002：代表开户 （代表开主账户二类户）JQ001:代表鉴权 CM2:主账户更改手机号码 BC101:绑卡前置校验
// BC102:绑卡 ZF001:支付OTP CM001：更新手机号 TI001：代表代扣 BC001：代表转账 CB001：换绑卡 B0001：购买订单
// B0002：赎回订单" "OA：代表三类户开户 TI001：代表代扣 BC：绑卡 CM3：修改三类户手机号 RB：解除绑卡 CA：销户 RH: 去除半年无交易"

    // 获取上传影像的Token接口
    @RequestMapping(value="/send",produces = "application/json; charset=utf-8")
    public @ResponseBody Map<String,String> send(@RequestBody Map<String, String> args) {
        Map<String, String> mapMeta = new HashMap<String, String>();
        StringBuffer log=new StringBuffer();
        Map<String, String> prame = new HashMap<String, String>();
        String action_code=args.get("action_code")==null?"":args.get("action_code");
        String method_str="action_code_"+action_code;
        log.append("方法"+method_str+"接受密文:"+JSON.toJSONString(args)+"\n");
        try{
            //RSA 解密
            prame = getRealDataMap(args);
            log.append("方法"+method_str+"接受数据:"+JSON.toJSONString(prame)+"\n");
            if(!prame.get("code").equals("000")){
                 return TradeDoArgs.return_error(args,prame.get("code"),prame.get("msg"));
            }
            log.append("数据解密后："+JSON.toJSONString(prame));
            //解密后数据处理
            String client_no = args.get("client_no");

            prame.put("client_no",client_no);
            erLeihuDataApi.setSuuid(suuid);
            erLeihuDataApi.setClient(client);
            erleihuRechargeApi.setSuuid(suuid);
            erleihuRechargeApi.setClient(client);
            if(action_code.equals("00")){
                erLeihuDataApi.setAgentArgs(agentArgs);
                return erLeihuDataApi.get_client_reg(prame);
            }

           return TradeDoArgs.return_error(args,"100000","接口类型码错误");

        } catch (Exception e) {
            return TradeDoArgs.return_error(args,"002","请求异常");

        }finally {
            logger.info(log.toString());
        }
    }

    /**
     * 判断参数，解密,验签
     * @param param
     * @param method_str
     * @return
     */
    public Map<String,String>  getRealDataMap(Map<String,String> param){
        Map<String,String> return_map=new HashMap<String,String> ();
        Map<String,String> headCheck=TradeDoArgs.checkParam("head",param);
        String code=headCheck.get("code");
        String msg=headCheck.get("msg");
        String suuid=UUID.randomUUID().toString();
        param.put("suuid",suuid);
        this.suuid=suuid;
        String action_code=param.get("action_code")==null?"":param.get("action_code");
        String method_str="action_code_"+action_code;
        logger.info("方法"+method_str+"接受数据:"+JSON.toJSONString(param));
        if(!code.equals("000")){
             return headCheck;
        }
        String uuid=param.get("uuid");
        String data=JSON.toJSONString(param.get("data"));
        try{
            String data_str=RSAUtil.privateDecrypt(data,RSAUtil.getPrivateKey(api_private_key)); //解密
            return_map = (Map) JSONObject.parse(data_str);

            Map<String,String> checkParamResult= TradeDoArgs.checkParam(method_str,return_map);
            if(!checkParamResult.get("code").equals("000")){
                 return  checkParamResult;
            }
            String sign=return_map.get("sign");
            return_map.remove("sign");  //验签不包含sign自身
            String client_no = param.get("client_no");
            String client_public_key="";
            if(action_code.equals("00")){
                agentArgs=redisCacheService.getAgentArgs(client_no);
                if (agentArgs == null) {
                    return_map.put("uuid", uuid);
                    return_map.put("suuid", suuid);
                    return_map.put("code", "003");
                    return_map.put("msg", "未匹配到渠道");
                    return return_map;
                }
                client_public_key=agentArgs.getPublic_key();
            }else {
                client = redisCacheService.getClient(client_no);
                if (client == null) {
                    return_map.put("uuid", uuid);
                    return_map.put("suuid", suuid);
                    return_map.put("code", "003");
                    return_map.put("msg", "未匹配到客户");
                    return return_map;
                }
                client_public_key = client.getClient_public_key();
            }
            boolean check_sign=RSAUtil.checkSignWithRSA(return_map,client_public_key,sign);
            return_map.put("uuid",uuid);
            return_map.put("suuid",suuid);
            if(check_sign) {
                if(action_code.equals("01")){
                    if(!param.containsKey("cert_pic_front")||param.get("cert_pic_front").equals("")){
                         return_map.put("code","005");
                         return_map.put("msg","参数cert_pic_front缺失");
                         return  return_map;
                    }
                    if(!param.containsKey("cert_pic_reverse")||param.get("cert_pic_reverse").equals("")){
                        return_map.put("code","005");
                        return_map.put("msg","参数cert_pic_reverse缺失");
                        return  return_map;
                    }
                    return_map.put("cert_pic_front",param.get("cert_pic_front"));
                    return_map.put("cert_pic_reverse",param.get("cert_pic_reverse"));
                }

                return_map.put("code","000");
                return return_map;
            }else{
                logger.info("识别号："+uuid+"验签失败");
                return_map.put("code","004");
                return_map.put("msg","验签失败");
                return return_map;
            }
        }catch (Exception e){
            logger.info("识别号："+uuid+"解密失败\n"+e);
            return_map.put("uuid",uuid);
            return_map.put("suuid",suuid);
            return_map.put("code","008");
            return_map.put("msg","解密失败");
            return return_map;
        }

    }
    /**
     * 加密返回
     * @param param
     * @param method_str
     * @return
     */
//    public  Map<String,String> returnSuccess(Map<String,String> param,String client_public_key,String api_private_key){
//        Map<String,String> return_map=new HashMap<String,String> ();
//        String uuid=UUID.randomUUID().toString();
//        try {
//            String sign = RSAUtil.signWithRSA(param, api_private_key);
//            param.put("sign",sign);
//            String data_str= JSON.toJSONString(param);
//            String data=RSAUtil.publicEncrypt(data_str,RSAUtil.getPublicKey(client_public_key));
//            return_map.put("data",data);
//            logger.info("成功返回数据:"+JSON.toJSONString(return_map));
//            return return_map;
//        }catch (Exception e){
//            logger.info("识别号："+uuid+"解密失败\n"+e);
//            return_map.put("status","error");
//            return return_map;
//        }
//    }



}