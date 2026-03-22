package com.lgyf.demo.util;

import com.alibaba.fastjson.JSON;
import com.lgyf.demo.controller.TradDoController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/***********************************************
 * @Author merry$
 * @Description //TODO $
 * @Date $ $
 * @Param $
 * @return $
 ***********************************************/
public class Return {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TradDoController.class);
    private static String api_private_key="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ5ra8w6cuzruOzOSspDFSq2BWGfIYT_QMBumgbKh1StDOQ7Uw3Fjcn7RQtyYBnx4w6S--p1xlVyAhE0T0gGL0EszD7mSffqQIfrsbGQT1_qXnlUerfKmSBZmQsd3rGh3ZdbfeiOne7k1_P424uUZYtAVv7niTseCmO4FW6KWdmdAgMBAAECgYBnOpZe2UNkMOIqpO5FSFs1nrB5AWmlMXMvMaL6g-SVf8IjwqiHE9El6a7_G4OVxHHxYEP5bs9ThFvmfBChXS5SonrxA35R_oyW5btwJpbNC2hMOrD71wSlu1cnaE6A4zY9pWq7LQQza9_dRFAbl86TVAye_FEc56W95M-j-lGa4QJBAMnh6kh170cNCobqaF-wbStMdnqVR-qv_4IfQgnA_lPAeP_pp_s7rX4aR909M-jl2gVDiPwjmgIjH6efr6H-3jkCQQDI4uNkxP8PE6NEBZnB43-z1ZsHzsYw60B7Oayw-kVlRSnYcEyK4AzBlOJT_uFaz26OD0zWu6ODp393tT3wk5aFAkBoIYtCzPIa5_MiHA6yLMJirxH9sLpjp1xXtd_OQCnu8Tx4ZAEtaV7XyJ6A02zPGKAYTvl9dK-fopZVU9y35kqhAkEAsTBfPiz8hIHfykneW0SdCTRp8DMUYWW_lHFEvz2hJDgjNsOm3__LgJwgHdJ9V6oLa8ZXEjije6asKhx1K9QiOQJBAMDg6GzzNEfeRK-IibqjsqEPyPc6BKoSTs1sFJxopd8KZyOOWnfRReWV7EIskuSJ79LEGhvoO_5xXuxzD2zIqw4=";

    private static String api_public_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCea2vMOnLs67jszkrKQxUqtgVhnyGE_0DAbpoGyodUrQzkO1MNxY3J-0ULcmAZ8eMOkvvqdcZVcgIRNE9IBi9BLMw-5kn36kCH67GxkE9f6l55VHq3ypkgWZkLHd6xod2XW33ojp3u5Nfz-NuLlGWLQFb-54k7HgpjuBVuilnZnQIDAQAB";

    public static String getApi_private_key() {
        return api_private_key;
    }

    public static void setApi_private_key(String api_private_key) {
        Return.api_private_key = api_private_key;
    }

    public static String getApi_public_key() {
        return api_public_key;
    }

    public static void setApi_public_key(String api_public_key) {
        Return.api_public_key = api_public_key;
    }

    public static Map<String,String> returnSuccess(Map<String,String> param, String client_no, String client_public_key, String lgyf_private_key){

        Map<String,String> return_map=new HashMap<String,String> ();
        String uuid="";
        if(!param.containsKey("uuid")){
            uuid= UUID.randomUUID().toString();
        }else{
            uuid=param.get("uuid");
            param.remove("uuid");
        }
        return_map.put("client_no",client_no);
        return_map.put("code","000");
        return_map.put("uuid",uuid);
        return_map.put("msg","成功");
        try {
            String sign = RSAUtil.signWithRSA(param, lgyf_private_key);
            param.put("sign",sign);
            String data_str= JSON.toJSONString(param);
            String data=RSAUtil.publicEncrypt(data_str,RSAUtil.getPublicKey(client_public_key));
            return_map.put("data",data);
            logger.info("成功返回数据:"+JSON.toJSONString(return_map));
            return return_map;
        }catch (Exception e){
            Map<String,String> map=new HashMap<>();
            map.put("client_no",client_no);
            map.put("uuid",uuid);
            return returnError(map,"002","系统加密失败");
        }

    }
    public static Map<String,String> returnError(Map<String,String> param,String err_code,String msg){
        String client_no=param.containsKey("client_no")?param.get("client_no"):"";
        String uuid="";
        if(!param.containsKey("uuid")){
            uuid= UUID.randomUUID().toString();
        }else{
            uuid=param.get("uuid");
        }
        Map<String,String> return_map=new HashMap<String,String> ();
        return_map.put("client_no",client_no);
        return_map.put("uuid",uuid);
        return_map.put("code",err_code);
        return_map.put("msg",msg);
        logger.info("错误返回数据："+JSON.toJSONString(return_map));
        return return_map;
    }

    public static String checkArgNull(String argkeys,Map<String,String> argmap){
          String[] argkeysArray=argkeys.split("\\,");
          for(String argkey:argkeysArray){
                if(!argmap.containsKey(argkey)){
                    return "参数["+argmap+"]缺失";
                }
                if(argmap.get(argkey)==null||argmap.get(argkey).toString().trim().equals("")){
                    return "参数["+argmap+"]必填";
                }
          }
          return "";
    }

}
