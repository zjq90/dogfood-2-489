package com.lgyf.demo.util;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class DepositUtil {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DepositUtil.class);
    private String PRV_KEY = ResourceBundle.getBundle("commondata").getString("PRV_KEY");
    private String IV_PARAM = ResourceBundle.getBundle("commondata").getString("IV_PARAM");
    private String se_url = ResourceBundle.getBundle("commondata").getString("se_url");
    private String up_url = ResourceBundle.getBundle("commondata").getString("up_url");
    private String orgNo = ResourceBundle.getBundle("commondata").getString("orgNo");
    static String app_id = ResourceBundle.getBundle("commondata").getString("appid");

    static String time = DateUtil.getDateFormate(new Date(),"YYYYMMDDHHMMSS");
    //押金查询
    public String recharge(String devNo){
        try{
//            se_url = se_url +"sxzs-ospi/activity/getDeviceDeposit";
            String ran = time;
            String transNonce = UUID.randomUUID().toString();
            Map meta = new HashMap();
            meta.put("transCode","S012");
            meta.put("transNonce",transNonce);
            meta.put("transDate",time);

            Map data = new HashMap();
            data.put("devNo",devNo);

            Map resMap = new HashMap();
            resMap.put("meta", meta);
            resMap.put("data",data);
            String reqJson = JSON.toJSONString(resMap);
//            logger.info("押金查询接口请求明文参数："+reqJson);
            String request = encrypt(reqJson);
//            logger.info("押金查询接口请求加密参数："+request);
            String msg = post(request,se_url,app_id);
            String respJson = decrypt(msg);
            logger.info("押金查询接口请求结果返回参数："+respJson);
            return  respJson;
        }catch (Exception e){
            logger.info("押金查询接口请求异常："+e);
            return null;
        }

    }

    //修改押金
    public String updateDeposit(String [] device_nos,String depositAmount,String activityNo) {
        try{
//            up_url = up_url+"sxzs-ospi/activity/updateDeviceDeposit";
            String transNonce = UUID.randomUUID().toString();
//            String [] device_nos =device_no.split("\\,");
            Map meta = new HashMap();
            meta.put("transCode","S013");
            meta.put("transNonce",transNonce);
            meta.put("transDate",time);

            Map data = new HashMap();
            data.put("devNos",device_nos);
            data.put("activityNo",activityNo);
            data.put("depositAmount",depositAmount);
            data.put("orgNo",orgNo);

            Map resMap = new HashMap();
            resMap.put("meta", meta);
            resMap.put("data",data);

            String reqJson = JSON.toJSONString(resMap);
            logger.info("押金修改接口请求明文参数："+reqJson);
            String encryptStr = encrypt(reqJson);//请求数据加密
//            logger.info("押金修改接口请求密文参数："+encryptStr);
            String resp = post(encryptStr,up_url,app_id);
            String decryptStr = decrypt(resp);
//            logger.info("押金修改接口请求结果返回参数："+decryptStr);
            return decryptStr;
        }catch (Exception e){
            logger.info("押金修改接口请求异常："+e);
            return null;
        }
    }

    public static String post(String content,String rurl,String app_id) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        String result = null;
        //指定信任管理器对象。
        URL console = new URL(rurl);
        HttpURLConnection conn = (HttpURLConnection) console.openConnection();
        conn.setDoOutput(true);
        //设置请求头
        conn.setRequestProperty("Content-Type", "text/x-json");
        conn.setRequestProperty("X-App-ID",app_id);
        conn.setConnectTimeout(60000);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes("UTF-8"));
        // 刷新、关闭
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();
        if (is != null) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            result = outStream.toString();
        }
        return result;
    }

    public  String encrypt(String request) throws Exception {
        byte[] content = request.getBytes(StandardCharsets.UTF_8);

        byte[] key = randomAESKey();
        byte[] encKey = RSA.encryptByPrivateKey(key, PRV_KEY);

        byte[] iv = Base64.getDecoder().decode(IV_PARAM);
        byte[] encVal = AES.encrypt(content, key, iv);

        byte[] encrypted = new byte[encKey.length + encVal.length];

        System.arraycopy(encKey, 0, encrypted, 0, encKey.length);
        System.arraycopy(encVal, 0, encrypted, encKey.length, encVal.length);

        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static byte[] randomAESKey() {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        return key;
    }

    public String decrypt(String response) throws Exception {
        byte[] content = Base64.getDecoder().decode(response);

        byte[] encKey = new byte[256];
        byte[] encVal = new byte[content.length - encKey.length];

        System.arraycopy(content, 0, encKey, 0, encKey.length);
        System.arraycopy(content, encKey.length, encVal, 0, encVal.length);

        byte[] key = RSA.decryptByPrivateKey(encKey, PRV_KEY);
        byte[] iv = Base64.getDecoder().decode(IV_PARAM);

        byte[] respVal = AES.decrypt(encVal, key, iv);
        return new String(respVal, StandardCharsets.UTF_8);
    }
}
