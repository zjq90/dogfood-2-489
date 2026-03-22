package com.lgyf.demo.util;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 驷口 -难而为之-
 * @date 2021/5/13 1:07
 */
public class SX_RSA {
    public static  String PRV_KEY;
    public static  String IV_PARAM;
    public static final String KEY_ALGORITHM = "RSA";
    public static final String YXF = "10";  //优选付
    public static final String SK = "20";   //刷卡收款
    public static final String JSD = "30";   //及时到
    public static final String YSF = "33";   //云闪付
    public static final String WSM = "40";   //微信扫码收款
    public static final String ZSM = "50";   //支付宝扫码收款
    public static final String YSM = "52";   //银联扫码收款

    public static String getPrvKey() {
        return PRV_KEY;
    }

    public static void setPrvKey(String prvKey) {
        PRV_KEY = prvKey;
    }

    public static String getIvParam() {
        return IV_PARAM;
    }

    public static void setIvParam(String ivParam) {
        IV_PARAM = ivParam;
    }

    /**
     * 加密
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static String encrypt(String request) throws Exception {
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


    public  static Map getYouMap(String en, String chuxu, String xinyong, BigDecimal txMoney){
        Map en_to_num=new HashMap();
        Map<String,Object> fee=new HashMap<String,Object>();
        en_to_num.put("trad_rate",SK);
        en_to_num.put("wx",WSM);
        en_to_num.put("zfb",ZSM);
        en_to_num.put("yl",YSM);

        if(en_to_num.containsKey(en)){
            fee.put("feeTypeId",en_to_num.get(en));
            fee.put("creditCardFee",chuxu);
            fee.put("debitCardFee",xinyong);
            fee.put("baseWithdrawFee",en.equals("trade_rate")?txMoney:0);  //只有刷卡才加单笔手续费
        }
        return fee;
    }


    /**
     * 随机AES密钥
     *
     * @return
     */
    private static byte[] randomAESKey() {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        return key;
    }

    /**
     * 解密
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static String decrypt(String response) throws Exception {
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
