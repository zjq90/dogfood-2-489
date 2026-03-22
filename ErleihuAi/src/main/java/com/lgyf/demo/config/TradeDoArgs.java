package com.lgyf.demo.config;

import com.alibaba.fastjson.JSON;
import com.lgyf.demo.util.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public  class TradeDoArgs {
    private static Logger logger = LoggerFactory.getLogger("trade");
     public static Map<String, Map<String,String>>  config(){
         Map<String, Map<String,String>>  args=new HashMap<String,Map<String,String>>();
         Map<String,String> head=new HashMap<>();
         head.put("client_no","客户编号,1,0,50,^[CA][0-9]{4}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母,3任意字符）
         head.put("version","版本号,1,0,3,^1.0$");
         head.put("uuid","跟踪号uuid,1,0,50,^[\\u4e00-\\u9fa5A-Za-z0-9-\\_]{8,50}$");
         head.put("action_code","指令,0,2,2,^[0-9]{2}$");
         head.put("cert_pic_front","身份证正面,0,0,0");
         head.put("cert_pic_reverse","身份证背面,0,0,0");
         args.put("head",head);

         Map<String,String> action_code_00=new HashMap<>();
         action_code_00.put("client_name","企业全称,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,30}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_00.put("short_name","企业简称,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,30}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_00.put("reg_client_no","企业编号,1,0,50,^C[0-9]{4}$");  //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_00.put("mobile","手机号,1,0,11,^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
         action_code_00.put("socket_ip","服务器IP,1,0,50,^(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})(,\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})*$");
         action_code_00.put("public_key","RSA公钥,1,0,300,^[0-9a-zA-Z_\\-+/=]{120,}$");
         args.put("action_code_00",action_code_00);
         //身份证上传
         Map<String,String> action_code_01=new HashMap<>();
         action_code_01.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_01.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         args.put("action_code_01",action_code_01);
         //短信返送
         Map<String,String> action_code_02=new HashMap<>();
         action_code_02.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_02.put("scene","短信场景号,1,0,5,^[A-Z0-9]{2,5}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_02.put("account_no","银行卡号,1,0,22,^[0-9]{10,22}$");
         action_code_02.put("mobile","预留手机号,1,0,11,^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
         action_code_02.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         args.put("action_code_02",action_code_02);

         //开通二类户
         Map<String,String> action_code_03=new HashMap<>();
         action_code_01.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_03.put("account_no","银行卡号,1,0,22,^[0-9]{10,22}$");
         action_code_03.put("mobile","预留手机号,1,0,11,^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
         action_code_03.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         action_code_03.put("client_ip","终端IP,1,0,50,^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$");
         action_code_03.put("device_id","设备编号,1,0,50");
         action_code_03.put("occupation","职业代码,1,0,5,^[0-9]{5}$");
         action_code_03.put("home_address","详细地址,1,0,99,^[a-zA-Z0-9\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{5,99}$");
         action_code_03.put("otp_code","短信验证码,1,0,50,^[0-9]{6}$");
         action_code_03.put("cert_order_no","身份证单号,1,0,50,^[a-zA-Z0-9]{10,50}$");
         action_code_03.put("otp_order_no","短信订单号,1,0,50,^[a-zA-Z0-9]{10,50}$");
         action_code_03.put("business_no","短信业务号,1,0,50,^[a-zA-Z0-9\\_\\-]{10,50}$");
         action_code_03.put("lbs","经度纬度,1,0,50");
         args.put("action_code_03",action_code_03);
         //余额查询签约
         Map<String,String> action_code_04=new HashMap<>();
         action_code_04.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_04.put("ele_account_no","二类账户号,1,0,22,^[0-9]{10,22}$");
         action_code_04.put("mobile","预留手机号,1,0,11,^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
         action_code_04.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         action_code_04.put("otp_order_no","短信订单号,1,0,50,^[a-zA-Z0-9]{10,50}$");
         action_code_04.put("otp_code","短信验证码,1,0,50,^[0-9]{6}$");
         action_code_04.put("business_no","短信业务号,1,0,50,^[a-zA-Z0-9\\_\\-]{10,50}$");
         args.put("action_code_04",action_code_04);

         //余额查询
         Map<String,String> action_code_05=new HashMap<>();
         action_code_05.put("agreement_no","签约协议号,1,0,50,^[a-zA-Z0-9\\_\\-]{10,50}$");
         action_code_05.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_05.put("ele_account_no","二类账户号,1,0,22,^[0-9]{10,22}$");
         action_code_05.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         args.put("action_code_05",action_code_05);


         //新增绑卡
         Map<String,String> action_code_08=new HashMap<>();
         action_code_08.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_08.put("account_no","银行卡号,1,0,22,^[0-9]{10,22}$");
         action_code_08.put("ele_account_no","二类账户号,1,0,22,^[0-9]{10,22}$");
         action_code_08.put("mobile","预留手机号,1,0,11,^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
         action_code_08.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         action_code_08.put("client_ip","终端IP,1,0,50,^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$");
         action_code_08.put("device_id","设备编号,1,0,50");
         action_code_08.put("device_system","设备系统,1,0,10,^[a-zA-Z0-9]{3,10}$");
         action_code_08.put("device_name","设备品牌,1,0,50");
         action_code_08.put("mac","设备MAC地址,1,0,17,^([0-9a-fA-F]{2})(([-][0-9a-fA-F]{2}){5})$");
         action_code_08.put("otp_code","短信验证码,1,0,50,^[0-9]{6}$");
         action_code_08.put("otp_order_no","短信订单号,1,0,50,^[a-zA-Z0-9]{10,50}$");
         action_code_08.put("business_no","短信业务号,1,0,50,^[a-zA-Z0-9\\_\\-]{10,50}$");
         action_code_08.put("lbs","经度纬度,1,0,50");
         args.put("action_code_08",action_code_08);



         //支付短信获取
         Map<String,String> action_code_12=new HashMap<>();
         action_code_12.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_12.put("ele_account_no","二类账户号,1,0,22,^[0-9]{10,22}$");
         action_code_12.put("mobile","预留手机号,1,0,11,^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
         action_code_12.put("amt","金额,1,0,11,^\\d+$|^\\d+\\.\\d{1,2}$");
         action_code_12.put("to_account_no","收款银行卡,1,0,22,^[0-9]{10,22}$");
         action_code_12.put("to_name","收款人,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         args.put("action_code_12",action_code_12);

          //支付
         Map<String,String> action_code_13=new HashMap<>();
         action_code_13.put("name","姓名,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_13.put("ele_account_no","二类账户号,1,0,22,^[0-9]{10,22}$");
         action_code_13.put("mobile","预留手机号,1,0,11,^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
         action_code_13.put("amt","金额,1,0,11,^\\d+$|^\\d+\\.\\d{1,2}$");
         action_code_13.put("to_account_no","收款银行卡,1,0,22,^[0-9]{10,22}$");
         action_code_13.put("to_name","收款人,1,0,20,^[a-zA-Z\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");   //X,Y,Z 分别代表 -》X是否必填（0非必填，1必填），Y数据类型（0 不限，1数字，2数字字母）
         action_code_13.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         action_code_13.put("client_ip","终端IP,1,0,50,^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$");
         action_code_13.put("otp_order_no","短信订单号,1,0,50,^[a-zA-Z0-9]{10,50}$");
         action_code_13.put("otp_code","短信验证码,1,0,50,^[0-9]{6}$");
         action_code_13.put("business_no","业务号,1,0,50,^[a-zA-Z0-9\\_\\-]{10,50}$");
         action_code_13.put("remark","出款备注,0,0,20");
         args.put("action_code_13",action_code_13);


         //支付结果查询

         Map<String,String> action_code_14=new HashMap<>();
         action_code_14.put("ele_account_no","二类账户号,1,0,22,^[0-9]{10,22}$");
         action_code_14.put("cert_no","身份证号,1,0,18,^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^([1-6][1-9]|50)\\d{4}\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}$");
         action_code_14.put("business_no","流水号,0,0,50,^[a-zA-Z0-9\\_\\-]{0,50}$");
         action_code_14.put("amt_order_no","转账指令号,0,0,50,^[a-zA-Z0-9\\_\\-]{0,50}$");
         args.put("action_code_14",action_code_14);
         return args;

     }


     public static Map<String,String> checkParam(String method_str,Map<String,String> data){
         Map<String,String> result=new HashMap<>();
         result.put("code","000");
         result.put("msg","");
         Map<String, Map<String,String>>  args=config();
         if(!args.containsKey(method_str)) {
             result.put("code","007");
             result.put("msg","当前指令不存在");
             return result;
         }
           Map<String,String> arg=args.get(method_str);
           for(String key:arg.keySet()){
               String args_rim=arg.get(key);
               String[] args_array=args_rim.split("\\,");
               if(args_array[1].equals("1")&&(!data.containsKey(key)||data.get(key).trim().equals(""))){  //判断是否必填
                     result.put("code","006");
                     result.put("msg",args_array[0]+"参数"+key+"不能缺失或不能为空");
                     return result;
                }
               if(data.containsKey(key)) {
                   String current_data = data.get(key);
                   Integer maxLen = Integer.parseInt(args_array[3]);
                   Integer dataType=Integer.parseInt(args_array[2]);
                   if(maxLen!=0) {            //0代表不限制长度
                       if (current_data.length() > maxLen) {
                           result.put("code", "006");
                           result.put("msg", args_array[0] + "参数" + key + "超过最大长度");
                           return result;
                       }
                   }
                   if(args_array.length==5){ //代表有正则
                        if(!args_array[4].equals("")&&!Pattern.matches(args_array[4], current_data)){
                            result.put("code", "006");
                            result.put("msg", args_array[0] + "参数" + key + "格式错误");
                            return result;
                        }
                   }else{
                       if(dataType!=0&&dataType!=3){
                            String pattern=dataType==1?"^[0-9]+$":"^[A-Za-z0-9]+$"; //数字字符较量
                            if(!Pattern.matches(pattern,current_data)){
                                result.put("code", "006");
                                result.put("msg", args_array[0] + "参数" + key + "格式错误");
                                return result;
                            }
                       }
                   }
               }
           }

         return result;
     }

    public static Map<String,String> return_error(Map<String,String> param,String err_code,String msg){
        String client_no=param.containsKey("client_no")?param.get("client_no"):"";
        String uuid="";
        if(!param.containsKey("uuid")){
            uuid= UUID.randomUUID().toString();
        }else{
            uuid=param.get("uuid");
        }
        String suuid=param.get("suuid")==null?"":param.get("suuid");
        Map<String,String> return_map=new HashMap<String,String> ();
        return_map.put("client_no",client_no);
        return_map.put("uuid",uuid);
        return_map.put("code",err_code);
        return_map.put("msg",msg);
        logger.info(suuid+"错误返回数据："+ JSON.toJSONString(return_map));
        return return_map;
    }

    public static Map<String,String> return_success(Map<String,String> param,String client_no,String client_public_key,String lgyf_private_key){
            String msg="成功";
            return return_success(param,client_no,client_public_key,lgyf_private_key,msg);
    }
    public static Map<String,String> return_success(Map<String,String> param,String client_no,String client_public_key,String lgyf_private_key,String msg){
        Map<String,String> return_map=new HashMap<String,String> ();
        String uuid="";
        String suuid="";
        if(!param.containsKey("uuid")){
            uuid= UUID.randomUUID().toString();
        }else{
            uuid=param.get("uuid");
            suuid=param.get("suuid")==null?"":param.get("suuid");
            param.remove("uuid");
            param.remove("suuid");
        }
        return_map.put("client_no",client_no);
        return_map.put("code","000");
        return_map.put("uuid",uuid);
        return_map.put("msg",msg);
        try {
            String sign = RSAUtil.signWithRSA(param, lgyf_private_key);
            param.put("sign",sign);
            String data_str= JSON.toJSONString(param);
            String data= RSAUtil.publicEncrypt(data_str,RSAUtil.getPublicKey(client_public_key));
            return_map.put("data",data);
            logger.info(suuid+"成功返回数据:"+ JSON.toJSONString(return_map));
            return return_map;
        }catch (Exception e){
            Map<String,String> map=new HashMap<>();
            map.put("client_no",client_no);
            map.put("uuid",uuid);
            return return_error(map,"002","系统加密失败");
        }

    }
}
