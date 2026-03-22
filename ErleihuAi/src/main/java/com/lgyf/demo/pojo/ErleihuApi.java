package com.lgyf.demo.pojo;

import com.alibaba.fastjson.JSON;
import com.lgyf.demo.util.SnowflakeIdWorker;
import com.pab.is.obp.easysdk.client.api.ErLeiHuZhiFuQianBao;
import com.pab.is.obp.easysdk.client.api.FuGongErLeiHu;
import com.pab.is.obp.easysdk.client.api.FuGongIiLeiHuYiQingIiLeiHuQianYiShangHuShiYong;
import com.pab.is.obp.easysdk.client.api.LingShouQuDaoZhuanYongIiLeiHu;
import com.pab.is.obp.easysdk.client.model.*;
import com.pingan.api.exception.ObpApiException;
import com.pingan.api.exception.ObpBeanValidateException;
import com.pingan.api.util.FileUploadRequest;
import com.pingan.api.util.FileUploadResponse;
import com.pingan.api.util.FileUtils;
import com.pingan.api.util.Generator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@Scope("prototype")
@Component
public class ErleihuApi {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ErleihuApi.class);
    private static String suuid;

    public String getSuuid() {
        return suuid;
    }

    public void setSuuid(String suuid) {
        this.suuid = suuid;
    }

    /**
     * 零售渠道专用II类户.获取上传影像的Token接口
     * 获取上传影像的Token接口
     *
     */
    public static Map<String, String>  obpApiIbankAcctWefileId(Map<String,String> args) {
        Map<String, String> Data = new HashMap<String, String>();
        String cert_pic_front_path=args.get("cert_pic_front_path");
        String cert_pic_reverse_path=args.get("cert_pic_reverse_path");
        String cert_no=args.get("cert_no");
        String name=args.get("name");

        StringBuffer log=new StringBuffer();
        log.append("身份证上传"+suuid+"\n");
        try {
            //获取上传影像的Token接口 youversion[上传影像]
            ObpApiIbankAcctWefileIdRequest obpApiIbankAcctWefileIdRequest = ObpApiIbankAcctWefileIdRequest.builder()
            .businessNo(Generator.generateBusinessNo())
            .fileType("jpg").build();
            ObpApiIbankAcctWefileIdResponse result_token = LingShouQuDaoZhuanYongIiLeiHu.create().obpApiIbankAcctWefileId(obpApiIbankAcctWefileIdRequest);
            log.append("suuid["+suuid+"]获取上传影像的Token接口:"+ JSON.toJSONString(result_token)+"\n");
            if(!result_token.getResponseCode().equals("000000")){
                  Data.put("code","fail");
                  Data.put("msg","上传影像:"+result_token.getResponseCode()+":"+result_token.getResponseMsg());
                  return Data;
            }
            if(!result_token.getBizCode().equals("000000")){
                Data.put("code","fail");
                Data.put("msg","上传影像"+result_token.getBizCode()+":"+result_token.getBizMsg());
                return Data;
            }
            //上传身份证正面
            FileUploadRequest fileUploadRequestFront = FileUploadRequest.builder()
            .charset("")
            .expiredTime(BigDecimal.valueOf(Long.parseLong(result_token.getExpiredTime())))
            .project(result_token.getProject())
            .file(new File(cert_pic_front_path))
            .url(result_token.getWefileUrl())
            .weFileToken(result_token.getWefileToken())
            .uuid(result_token.getUuid()).build();
            FileUploadResponse result_file_front = FileUtils.upload(fileUploadRequestFront);
            log.append("上传正面返回:"+ JSON.toJSONString(result_file_front)+"\n");
            if(!result_file_front.getResponseCode().equals("000000")){
                Data.put("code","fail");
                Data.put("msg","上传正面"+result_file_front.getResponseCode()+":"+result_file_front.getResponseMsg());
                return Data;
            }
            //上传身份证反面
            FileUploadRequest fileUploadRequestReverse = FileUploadRequest.builder()
                    .charset("")
                    .expiredTime(BigDecimal.valueOf(Long.parseLong(result_token.getExpiredTime())))
                    .project(result_token.getProject())
                    .file(new File(cert_pic_reverse_path))
                    .url(result_token.getWefileUrl())
                    .weFileToken(result_token.getWefileToken())
                    .uuid(result_token.getUuid()).build();
            FileUploadResponse result_file_reverse = FileUtils.upload(fileUploadRequestReverse);
            log.append("上传反面:"+ JSON.toJSONString(result_file_reverse)+"\n");
            if(!result_file_reverse.getResponseCode().equals("000000")){
                Data.put("code","fail");
                Data.put("msg","上传反面"+result_file_reverse.getResponseCode()+":"+result_file_reverse.getResponseMsg());
                return Data;
            }
            String imgOrderNo= SnowflakeIdWorker.generateId()+"";
            String frontSide=result_file_front.getData().getFileId();
            String backSide=result_file_reverse.getData().getFileId();
            //影像审核
            ObpApiIbankAcctGetIdImageInfoAnduploadResponse certPassResponse=obpApiIbankAcctGetIdImageInfoAndupload(imgOrderNo,frontSide, backSide,cert_no,name);
            log.append("影像审核:"+JSON.toJSONString(certPassResponse)+"\n");
            if(certPassResponse==null){
                Data.put("code","fail");
                Data.put("msg","影像审核异常");
                return Data;
            }
            if(!certPassResponse.getResponseCode().equals("000000")){
                Data.put("code","fail");
                Data.put("msg","影像审核"+certPassResponse.getResponseCode()+":"+certPassResponse.getResponseMsg());
                return Data;
            }
            if(!certPassResponse.getBizCode().equals("000000")){
                Data.put("code","fail");
                Data.put("msg","影像审核"+certPassResponse.getBizCode()+":"+certPassResponse.getBizMsg());
                return Data;
            }
            String checkResult=certPassResponse.getCheckResult();
            Data.put("cert_order_no",imgOrderNo);
            Data.put("code",checkResult.equals("0")?"success":"fail");
            Data.put("msg",checkResult.equals("0")?"成功":"失败");
            return Data;
        } catch (Exception e) {
            Data.put("code","fail");
            Data.put("msg","上传身份证异常："+e.getMessage());
            return Data;
        }finally {
            log.append("最终结果："+JSON.toJSONString(Data));
            logger.info(log.toString());
        }
    }

    /**
     * 零售渠道专用II类户.余额查询
     * 查询余额接口
     *
     */
    public static ObpApiIbankAcctQryBalanceInfoResponse obpApiIbankAcctQryBalanceInfo()
            throws ObpApiException,ObpBeanValidateException{

        ObpApiIbankAcctQryBalanceInfoRequest obpApiIbankAcctQryBalanceInfoRequest = ObpApiIbankAcctQryBalanceInfoRequest.builder()
                // Required Properties c33b8f0dac46faa43cd05a9d686fa1
                .thirdId("53764861")
                .tradeNoId("tradeNoId_1597050500001")//订单号
                .acctNoArray("6226601234566696")
                .trueName("李定超")
                .idNo("511524199504252296")
                .idType("1")
                .isConvert("0")
                .isNeedRitianli("N")
                .requestTime("2021-10-08 17:08:25")
                // Optional Properties
                .bussType("null")
                .ccy("RMB")
//          .imputationFlag("null")
//          .ccyShowFlag("人民币")
//          .qryAddCardFlag("null")
//          .isQueryFlag("null")
                .isExemptOtp("").build();

        try {
            ObpApiIbankAcctQryBalanceInfoResponse result =
                    LingShouQuDaoZhuanYongIiLeiHu.create().obpApiIbankAcctQryBalanceInfo(obpApiIbankAcctQryBalanceInfoRequest);
            System.out.println(result);
            return result;
        } catch (ObpApiException e) {
            System.err.println("Exception when calling LingShouQuDaoZhuanYongIiLeiHu#obpApiIbankAcctQryBalanceInfo");
            throw e;
        } catch (ObpBeanValidateException e){
            // TODO params verification failed, e.violations indicates the field set that failed the verification
            System.err.println("BeanValidateException when calling LingShouQuDaoZhuanYongIiLeiHu#obpApiIbankAcctQryBalanceInfo");
            throw e;
        }
    }


    /**
     * 零售渠道专用II类户.影像审核
     * 影像审核接口，frontSide（人像面身份证图片）、backSide（国徽面身份证图片），每张图片500kb上限，图片必须清晰，不能缺边少角，不能有阴影，不能有手指挡住，身份证上面（国徽面）的有效期限必须与年龄匹配。3-6秒实时返回结果。上传成功后10分钟内有效。测试环境上传影像请先联系银行同事添加挡板。为提高通过率，建议用真实身份证。
     * @param  businessNo 订单号
     * @param  frontSide 人像面身份证图片
     * @param  backSide 国徽面身份证图片
     */
    public static ObpApiIbankAcctGetIdImageInfoAnduploadResponse obpApiIbankAcctGetIdImageInfoAndupload(String businessNo, String frontSide, String backSide,String id_card,String trueName) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctGetIdImageInfoAnduploadRequest obpApiIbankAcctGetIdImageInfoAnduploadRequest = ObpApiIbankAcctGetIdImageInfoAnduploadRequest.builder()
            .imageOrderNo(businessNo)
            .idNo(id_card)
            .idType("1")
            .frontSide(frontSide)
            .backSide(backSide)
            .trueName(trueName).build();
            logger.info("影像审核请求["+suuid+"]："+obpApiIbankAcctGetIdImageInfoAnduploadRequest.toString());
            ObpApiIbankAcctGetIdImageInfoAnduploadResponse result = LingShouQuDaoZhuanYongIiLeiHu.create().obpApiIbankAcctGetIdImageInfoAndupload(obpApiIbankAcctGetIdImageInfoAnduploadRequest);
            logger.info("影像审核返回["+suuid+"]"+JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            logger.info("影像审核异常["+suuid+"]:"+e.getMessage());
            return null;
        }
    }


    /**
     * 零售渠道专用II类户.开户前置校验职业家庭地址接口
     * 开户前对用户家庭地址、职业代码和工作单位进行校验
     *
     */
    public static ObpApiIbankAcctCheckUserInfoResponse obpApiIbankAcctCheckUserInfo(String trueName,String id_card,String homeAddress,String homeAddressDetail)
            throws ObpApiException,ObpBeanValidateException{

        ObpApiIbankAcctCheckUserInfoRequest obpApiIbankAcctCheckUserInfoRequest = ObpApiIbankAcctCheckUserInfoRequest.builder()
                // Optional Properties
                .trueName(trueName)
                .idNo(id_card)
                .idType("1")
                .homeAddress(homeAddress)// 非必填
                .homeAddressDetail(homeAddressDetail).build();// 非必填
        try {
            ObpApiIbankAcctCheckUserInfoResponse result = LingShouQuDaoZhuanYongIiLeiHu.create().obpApiIbankAcctCheckUserInfo(obpApiIbankAcctCheckUserInfoRequest);
            System.out.println(result);
            return result;
        } catch (ObpApiException e) {
            System.err.println("Exception when calling LingShouQuDaoZhuanYongIiLeiHu#obpApiIbankAcctCheckUserInfo");
            throw e;
        } catch (ObpBeanValidateException e){
            // TODO params verification failed, e.violations indicates the field set that failed the verification
            System.err.println("BeanValidateException when calling LingShouQuDaoZhuanYongIiLeiHu#obpApiIbankAcctCheckUserInfo");
            throw e;
            //  obpApiSendOtp
        }
    }


    /**
     * 零售渠道专用II类户.otp发送接口
     * OTP发送接口，开户接口的otp发送次数控制为1分钟一次，1小时10次。代扣/转账：10秒一次，1小时30次。 支付：30分钟内最多发送10次、验证码有效期2分钟
     * @param accopt 订单号
     */
    public static ObpApiIbankCommonSendOtpResponse obpApiIbankCommonSendOtp(String accopt,String scene,String trueName,String id_card,String bindCardNo,String mobileNo) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankCommonSendOtpRequest obpApiIbankCommonSendOtpRequest = ObpApiIbankCommonSendOtpRequest.builder()
            .businessNo(accopt)
            .scene(scene)
            .bizKey("{ \"trueName\":\""+trueName+"\", \"idNo\":\""+id_card+"\", \"idType\":\"1\", \"bindCardNo\":\""+bindCardNo+"\", \"mobileNo\":\""+mobileNo+"\" }")
            .mobileNo(mobileNo).build();
            System.out.println(suuid+"otp发送"+scene+"请求参数："+JSON.toJSONString(obpApiIbankCommonSendOtpRequest));
            logger.info(suuid+"otp发送"+scene+"请求参数："+JSON.toJSONString(obpApiIbankCommonSendOtpRequest));
            ObpApiIbankCommonSendOtpResponse result = LingShouQuDaoZhuanYongIiLeiHu.create().obpApiIbankCommonSendOtp(obpApiIbankCommonSendOtpRequest);
            logger.info(suuid+"otp发送"+scene+"返回结果："+JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            logger.info(suuid+"otp发送"+scene+"异常:",e.getMessage());
            return null;
        }
    }
    /**
     * 复工II类户_疫情II类户迁移商户使用.电子账户变更手机号
     * 变更二类户卡的预留手机号。针对用户已经把（他行）绑定卡的银行预留手机号更换了，需要更新我行的二类户预留手机号码
     *
     */
    public static ObpApiIbankAcctChangeMobileNoResponse obpApiIbankAcctChangeMobileNo(String thirdId, String businessNo,String trueName,String id_card, String accountNo,String bindCardNo,String mobileNo,String scene,String otpOrderNo,String otpValue) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctChangeMobileNoRequest obpApiIbankAcctChangeMobileNoRequest = ObpApiIbankAcctChangeMobileNoRequest.builder()
            .thirdId(thirdId)
            .businessNo(businessNo)
            .trueName(trueName)
            .idNo(id_card)
            .idType("1")
            .accountNo(accountNo)
            .bindCardNo(bindCardNo)
            .mobileNo(mobileNo)
            .scene(scene)
            .otpOrderNo(otpOrderNo)
            .otpValue(otpValue)
            .build();
            logger.info("电子账户变更手机号接口请求参数："+obpApiIbankAcctChangeMobileNoRequest.toString());
            ObpApiIbankAcctChangeMobileNoResponse result = FuGongIiLeiHuYiQingIiLeiHuQianYiShangHuShiYong.create().obpApiIbankAcctChangeMobileNo(obpApiIbankAcctChangeMobileNoRequest);
            logger.info("电子账户变更手机号接口返回结果："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("电子账户变更手机号接口异常:"+methodName+"========"+e);
            throw e;
        }
    }





    /**
     * 复工二类户.三类户绑卡前置校验
     * 三类户绑卡前置校验
     *
     */
    public static  ObpApiIbankAcctBindCardPreCheckResponse obpApiIbankAcctBindCardPreCheck(String businessNo,String scene, String customerName,String id_card,String accountNo,String bindCardNo,String mobileNo,String otpOrderNo,String otpValue,String terminalType,String appName,String sourceIP,String ipType,String deviceID,String deviceType, String fullDeviceNumber,String deviceName,String lbs,String simCardCount,String macAddr) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctBindCardPreCheckRequest obpApiIbankAcctBindCardPreCheckRequest = ObpApiIbankAcctBindCardPreCheckRequest.builder()
            .businessNo(businessNo)
            .customerName(customerName)
            .idNo(id_card)
            .idType("1")
            .accountNo(accountNo)
            .bindCardNo(bindCardNo) //绑定卡卡号
            .mobileNo(mobileNo)
            .scene(scene)
            .otpOrderNo(otpOrderNo) //otpOrderNo  返回的
            .otpValue(otpValue)
            .terminalType(terminalType)
            .appName(appName)
            .sourceIP(sourceIP)
            .ipType(ipType)
            .deviceID(deviceID)
            .deviceType(deviceType)
            .fullDeviceNumber(fullDeviceNumber)
            .deviceName(deviceName)
            .lbs(lbs)
            .simCardCount(simCardCount)
            .macAddr(macAddr).build();
            logger.info("三类户绑卡前置校验接口请求参数："+obpApiIbankAcctBindCardPreCheckRequest.toString());
            ObpApiIbankAcctBindCardPreCheckResponse result = FuGongErLeiHu.create().obpApiIbankAcctBindCardPreCheck(obpApiIbankAcctBindCardPreCheckRequest);
            logger.info("三类户绑卡前置校验接口返回结果："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("三类户绑卡前置校验接口异常:"+methodName+"========"+e);
            throw e;
        }
    }

    /**
     * 复工二类户.复工二类户开户
     * 复工二类户开户
     * @param merNo。第三方商户编号  后续操作都要用到
     * @param addno 业务请求流水号要求全局唯一。需要和OTP发送接 businessNo 一样  obpApiIbankCommonSendOtp
     * @param imageOrderNo 上传身份证返回的订单号
     * @param otpOrderNo。OTP订单号 发OTP短信接口返回的OTP订单号
     * @param otpCode。短信验证码
     */
    public static ObpApiIbankAcctReturnWorkOpenAccountResponse obpApiIbankAcctReturnWorkOpenAccount(String openAccountId,String thirdId,String businessNo,String imageOrderNo,String otpOrderNo,String otpCode, String trueName,String id_card,String bindCardNo,String mobileNo,String scene,String occupation,String clientIP,String lbs,String deviceID,String fullDeviceNumber, String appName,String homeAddress) throws ObpApiException,ObpBeanValidateException {
        try {
//            Integer a = (int) (1 + Math.random() * (100000000 - 1 + 1));
//            Integer orderiD = (int) (1 + Math.random() * (100000000 - 1 + 1));
            ObpApiIbankAcctReturnWorkOpenAccountRequest obpApiIbankAcctReturnWorkOpenAccountRequest = ObpApiIbankAcctReturnWorkOpenAccountRequest.builder()
            .thirdId(thirdId)//第三方会员ID
            .businessNo(businessNo) //业务请求流水号要求全局唯一。需要和OTP发送接 businessNo 一样  obpApiIbankCommonSendOtp
            .openAccountId(openAccountId)//
            .imageOrderNo(imageOrderNo)//影像订单号  obpApiIbankAcctGetIdImageInfoAndupload
            .trueName(trueName)
            .idNo(id_card)
            .idType("1")
            .bindCardNo(bindCardNo)//绑定卡卡号
            .mobileNo(mobileNo)
            .scene(scene)
            .otpOrderNo(otpOrderNo)//OTP订单号 发OTP短信接口返回的OTP订单号
            .otpValue(otpCode) //短信验证码
            .occupation(occupation) //职业代码  （建议商户维护职业列表
            .clientIP(clientIP)//客户端IP（监管需要）
            .lbs(lbs)//LBS信息（监管需要）
            .deviceID(deviceID)//设备标识（监管需要）
            .fullDeviceNumber(fullDeviceNumber)//设备SIM卡号（监管需要）
            .homeAddress(homeAddress)
             .appName(appName).build(); //交易发起应用名称（监管需要）
            // Optional Properties
//                .homeAddress("广东省深圳市罗湖区深南东路5047号")
//                .simCardCount("1")
//                .deviceType("1")
//                .deviceName("iPhone")
//                .macAddr("00-23-5A-15-99-42")
//                .ipType("04")
//                .umCode("CAIJING002")
//                .isAuthorization("0")
//                .workOrgName("广东省深圳市罗湖区深南东路5047号")
//                .source("ob0000001")
//                .outerSource("ob0000001")
//                .cid("ob0000001")
//                .outerid("ob0000001")
//                .channelType("PC、IOS、Android、HTML5")
//                .gps("gps定位信息")
//                .device("设备指纹")
//                .homeStreetAddress("民治街道")
//                .homeAddressDetail("民治大道沙吓老村11栋").build();
            System.out.println("复工二类户开户接口请求参数：" + JSON.toJSONString(obpApiIbankAcctReturnWorkOpenAccountRequest));
            logger.info("复工二类户开户接口请求参数：" + obpApiIbankAcctReturnWorkOpenAccountRequest.toString());
            ObpApiIbankAcctReturnWorkOpenAccountResponse result = FuGongErLeiHu.create().obpApiIbankAcctReturnWorkOpenAccount(obpApiIbankAcctReturnWorkOpenAccountRequest);
            logger.info("复工二类户开户接口返回结果：" + result);
            return result;
        } catch (ObpApiException e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("复工二类户开户接口异常:" + methodName + "========" + e);
            throw e;
        }
    }



    /**
     * 复工二类户.三类户绑卡前置校验结果查询
     * 三类户绑卡前置校验结果查询
     *
     */
    public static ObpApiIbankAcctQryBindCardPreCheckResultResponse obpApiIbankAcctQryBindCardPreCheckResult(String businessNo) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctQryBindCardPreCheckResultRequest obpApiIbankAcctQryBindCardPreCheckResultRequest = ObpApiIbankAcctQryBindCardPreCheckResultRequest.builder().businessNo(businessNo).build();
            logger.info("三类户绑卡前置校验结果查询接口请求参数："+obpApiIbankAcctQryBindCardPreCheckResultRequest.toString());
            ObpApiIbankAcctQryBindCardPreCheckResultResponse result = FuGongErLeiHu.create().obpApiIbankAcctQryBindCardPreCheckResult(obpApiIbankAcctQryBindCardPreCheckResultRequest);
            logger.info("三类户绑卡前置校验结果查询接口返回结果："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("三类户绑卡前置校验结果查询接口异常:"+methodName+"========"+e);
            throw e;
        }
    }

    /**
     * 复工二类户.复工二类户余额查询
     * 复工二类户余额查询
     *
     */
    public static ObpApiIbankAcctQryWorkBalanceInfoResponse obpApiIbankAcctQryWorkBalanceInfo(String businessNo, String thirdId,String tradeNoId,String acctNoArray,String trueName,String id_card,String requestTime,String agreementNo) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctQryWorkBalanceInfoRequest obpApiIbankAcctQryWorkBalanceInfoRequest = ObpApiIbankAcctQryWorkBalanceInfoRequest.builder()
            .businessNo(businessNo)
            .thirdId(thirdId)
            .tradeNoId(tradeNoId)
            .acctNoArray(acctNoArray)
            .trueName(trueName)
            .idNo(id_card)
            .idType("1")
            .isConvert("0")
            .isNeedRitianli("N")
            .requestTime(requestTime)
            .ccy("RMB")
            .isExemptOtp("0")
            .agreementNo(agreementNo)
              .build();
            System.out.println("复工二类户余额查询:"+JSON.toJSONString(obpApiIbankAcctQryWorkBalanceInfoRequest));
            logger.info("复工二类户余额查询接口请求参数："+obpApiIbankAcctQryWorkBalanceInfoRequest.toString());
            ObpApiIbankAcctQryWorkBalanceInfoResponse result = FuGongErLeiHu.create().obpApiIbankAcctQryWorkBalanceInfo(obpApiIbankAcctQryWorkBalanceInfoRequest);
            logger.info("复工二类户余额查询接口返回结果："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("复工二类户余额查询接口异常:"+methodName+"========"+e);
            throw e;
        }
    }

    /**
     * 复工二类户.三类户新增绑卡
     * 三类户新增绑卡
     *
     */
    public static ObpApiIbankAcctAddBindCardResponse obpApiIbankAcctAddBindCard(String businessNo,String customerName, String id_card,String accountNo,String bindCardNo,String mobileNo,String authSerialNo) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctAddBindCardRequest obpApiIbankAcctAddBindCardRequest = ObpApiIbankAcctAddBindCardRequest.builder()
                    // Required Properties
            .businessNo(businessNo)
            .customerName(customerName)
            .idNo(id_card)
            .idType("1")
            .accountNo(accountNo)
            .bindCardNo(bindCardNo)
            .mobileNo(mobileNo)
            .authSerialNo(authSerialNo).build();  //前置校验查询放回流水号
            logger.info("三类户新增绑卡接口请求参数："+obpApiIbankAcctAddBindCardRequest.toString());
            ObpApiIbankAcctAddBindCardResponse result = FuGongErLeiHu.create().obpApiIbankAcctAddBindCard(obpApiIbankAcctAddBindCardRequest);
            logger.info("三类户新增绑卡接口返回结果："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("三类户新增绑卡接口异常:"+methodName+"========"+e);
            throw e;
        }
    }

    /**
     * 余额查询签约
     * @param businessNo
     * @param thirdId
     * @param mobileNo
     * @param idNo
     * @param trueName
     * @param accountNo
     * @param otpOrderNo
     * @param otpValue
     * @return
     * @throws Exception
     */
    public static ObpApiIbankAcctQryWorkBalanceInfoSignResponse  obpApiIbankAcctQryWorkBalanceInfoSign(String businessNo,String thirdId,String mobileNo,String idNo,String trueName,String accountNo,String otpOrderNo,String otpValue) throws Exception{
        try{
            ObpApiIbankAcctQryWorkBalanceInfoSignRequest obpApiIbankAcctQryWorkBalanceInfoSignRequest=ObpApiIbankAcctQryWorkBalanceInfoSignRequest.builder()
                    .businessNo(businessNo)
                    .thirdId(thirdId)
                    .mobileNo(mobileNo)
                    .idType("1")
                    .idNo(idNo)
                    .trueName(trueName)
                    .accountNo(accountNo)
                    .otpOrderNo(otpOrderNo)
                    .otpValue(otpValue).build();
            logger.info("查询余额签约请求参数："+obpApiIbankAcctQryWorkBalanceInfoSignRequest.toString());
            ObpApiIbankAcctQryWorkBalanceInfoSignResponse result = FuGongErLeiHu.create().obpApiIbankAcctQryWorkBalanceInfoSign(obpApiIbankAcctQryWorkBalanceInfoSignRequest);
            logger.info("查询余额签约返回结果："+result);
            return result;
        }catch (Exception e){
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("查询余额签约异常:"+methodName+"========"+e);
            throw e;
        }
    }
    /**
     * 签约查询
     */
    public static ObpApiIbankAcctQryWorkBalanceSignInfoResponse obpApiIbankAcctQryWorkBalanceSignInfo(String businessNo,String thirdId,String mobileNo,String trueName,String accountNo) throws Exception {
        try {
            ObpApiIbankAcctQryWorkBalanceSignInfoRequest obpApiIbankAcctQryWorkBalanceSignInfoRequest = ObpApiIbankAcctQryWorkBalanceSignInfoRequest.builder()
                    .businessNo(businessNo)
                    .thirdId(thirdId)
                    .mobileNo(mobileNo)
                    .idType("1")
                    .trueName(trueName)
                    .accountNo(accountNo).build();
            logger.info("查询余额签约请求参数：" + obpApiIbankAcctQryWorkBalanceSignInfoRequest.toString());
            ObpApiIbankAcctQryWorkBalanceSignInfoResponse result = FuGongErLeiHu.create().obpApiIbankAcctQryWorkBalanceSignInfo(obpApiIbankAcctQryWorkBalanceSignInfoRequest);
            logger.info("查询余额签约返回结果：" + result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("查询余额签约异常:" + methodName + "========" + e);
            throw e;
        }
    }


    /**
     * 复工二类户.三类户解除绑卡
     * 解除绑定卡关系，只有一张卡不能解绑
     *
     */
    public static ObpApiIbankAcctDelBindCardResponse obpApiIbankAcctDelBindCard(String scene,String otpOrderNo,String otpValue,String businessNo,String customerName, String id_card,String accountNo,String unBindCardNo) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctDelBindCardRequest obpApiIbankAcctDelBindCardRequest = ObpApiIbankAcctDelBindCardRequest.builder()
                    // Required Properties
            .businessNo(businessNo)
            .customerName(customerName)
            .idNo(id_card)
            .idType("1")
            .accountNo(accountNo)
            .unBindCardNo(unBindCardNo)
            .scene(scene)
            .otpOrderNo(otpOrderNo)//返回的订单号
            .otpValue(otpValue).build();
            logger.info("三类户解除绑卡接口请求参数："+obpApiIbankAcctDelBindCardRequest.toString());
            ObpApiIbankAcctDelBindCardResponse result = FuGongErLeiHu.create().obpApiIbankAcctDelBindCard(obpApiIbankAcctDelBindCardRequest);
            logger.info("三类户解除绑卡接口返回结果："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("三类户解除绑卡接口异常:"+methodName+"========"+e);
            throw e;
        }
    }


    /**
     * 零售渠道专用II类户.otp发送接口 查询余额
     * OTP发送接口，查询余额的otp发送次数控制为1分钟一次，1小时10次。代扣/转账：10秒一次，1小时30次。 支付：30分钟内最多发送10次、验证码有效期2分钟
     *
     */
    public static ObpApiIbankObpApiSendOtpResponse obpApiIbankObpApiSendOtp(String bno,String trueName,String id_card,String mobileNo,String accountNo,String scene)
            throws ObpApiException,ObpBeanValidateException{

        ObpApiIbankObpApiSendOtpRequest obpApiIbankObpApiSendOtpRequest = ObpApiIbankObpApiSendOtpRequest.builder()
                // Required Properties  73616440
                .businessNo(bno)
                .scene(scene)
                //           .bizKey("{    \"trueName\":\"李定超\",    \"idNo\":\"511524199504252296\",    \"idType\":\"1\",    \"mobileNo\":\"13661386274\",\"bindCardNo\":\"6226601234566696\" }")
                .bizKey("{\"mobileNo\":\""+mobileNo+"\",\"idType\":\"1\",\"idNo\":\""+id_card+"\",\"trueName\":\""+trueName+"\",\"accountNo\":\""+accountNo+"\"}")
                // Optional Properties
                .accountNo(accountNo)
                .mobileNo(mobileNo).build();


        try {
            ObpApiIbankObpApiSendOtpResponse result =
                    ErLeiHuZhiFuQianBao.create().obpApiIbankObpApiSendOtp(obpApiIbankObpApiSendOtpRequest);
            System.out.println("签约发送短信："+JSON.toJSONString(obpApiIbankObpApiSendOtpRequest));
            return result;
        } catch (ObpApiException e) {
            System.err.println("Exception when calling LingShouQuDaoZhuanYongIiLeiHu#obpApiIbankCommonSendOtp");
            throw e;
        } catch (ObpBeanValidateException e){
            // TODO params verification failed, e.violations indicates the field set that failed the verification
            System.err.println("BeanValidateException when calling LingShouQuDaoZhuanYongIiLeiHu#obpApiIbankCommonSendOtp");
            throw e;
        }
    }
}
