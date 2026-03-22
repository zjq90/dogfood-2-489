package com.lgyf.demo.pojo;

import com.pab.is.obp.easysdk.client.api.LingShouQuDaoZhuanYongIiLeiHuChongZhiTiXian;
import com.pab.is.obp.easysdk.client.model.*;
import com.pingan.api.exception.ObpApiException;
import com.pingan.api.exception.ObpBeanValidateException;
import org.springframework.stereotype.Component;

@Component
public class RechargeApi {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RechargeApi.class);
    //扣款账号
    static String fromAccNo = "";
    //第三方商户编号 开户时提交
    static String thirdId = "70466551";

    /**
     * 零售渠道专用II类户充值提现.代扣银行卡限额查询
     * 代扣银行卡限额查询
     *
     */
    public static ObpApiIbankAcctQueryBankTransferLimitSmartResponse obpApiIbankAcctQueryBankTransferLimitSmart(String businessNo,String cardNos) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctQueryBankTransferLimitSmartRequest obpApiIbankAcctQueryBankTransferLimitSmartRequest = ObpApiIbankAcctQueryBankTransferLimitSmartRequest.builder()
                    // Required Properties
            .businessNo(businessNo)
            .queryType("0")
            .isCardSign("0")
                    // Optional Properties
//          .bankNames("平安银行")
            .cardNos(cardNos).build();
            logger.info("代扣银行卡限额查询接口请求参数："+obpApiIbankAcctQueryBankTransferLimitSmartRequest.toString());
            ObpApiIbankAcctQueryBankTransferLimitSmartResponse result = LingShouQuDaoZhuanYongIiLeiHuChongZhiTiXian.create().obpApiIbankAcctQueryBankTransferLimitSmart(obpApiIbankAcctQueryBankTransferLimitSmartRequest);
            System.out.println(result);
            logger.info("代扣银行卡限额查询接口请求参数："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("代扣银行卡限额查询接口异常:"+methodName+"========"+e);
            throw e;
        }
    }

    /**
     * 零售渠道专用II类户充值提现.出金转账结果查询
     * 转账结果查询
     *
     */
    public static ObpApiIbankAcctToTransferResultResponse obpApiIbankAcctToTransferResult(String bno,String orderSerialNo,String thirdId,String accountNo) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctToTransferResultRequest obpApiIbankAcctToTransferResultRequest = ObpApiIbankAcctToTransferResultRequest.builder()
                    // Required Properties
            .thirdId(thirdId)
            .accountNo(accountNo)
                    // Optional Properties
            .bussinessNo(bno)
            .orderSerialNo(orderSerialNo).build(); //响应流水号
            logger.info("出金转账结果查询接口请求参数："+obpApiIbankAcctToTransferResultRequest.toString());
            ObpApiIbankAcctToTransferResultResponse result = LingShouQuDaoZhuanYongIiLeiHuChongZhiTiXian.create().obpApiIbankAcctToTransferResult(obpApiIbankAcctToTransferResultRequest);
            System.out.println(result);
            logger.info("出金转账结果查询接口请求参数："+result);
            return result;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("出金转账结果查询接口异常:"+methodName+"========"+e);
            throw e;
        }
    }
    /**
     * 零售渠道专用II类户充值提现.出金转账接口
     * 转账接口。将平安银行二类户卡上的钱转账到绑定卡上。生产环境一般都是10秒钟左右到账，没有单笔单日限额。测试环境请联系银行同事加银行卡挡板
     *
     */
    public ObpApiIbankAcctToTransferResponse obpApiIbankAcctToTransfer(String businessNo,String poneCode,String otpOrderNo,String thirdId,String toAccountNo,String accountName,String bussinessScence,String amount,String fromAccountNo,String fromAccountName, String ipAddr,String remark) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankAcctToTransferRequest obpApiIbankAcctToTransferRequest = ObpApiIbankAcctToTransferRequest.builder()
                    // Required Properties
            .thirdId(thirdId)
            .toAccountNo(toAccountNo)
            .accountName(accountName)
            .bussinessNo(businessNo)//流水号
            .bussinessScence(bussinessScence)
            .channelType("H5")
            .currType("RMB")
            .duplicateConfirmFlag("N")
            .executeType("3")
            .amount(amount)
            .fromAccountNo(fromAccountNo)
            .fromAccountName(fromAccountName)
            .ipAddr(ipAddr)
            .limitConfirmFlag("N")
             .userRemark(remark)
            .otpOrderNo(otpOrderNo)  //opt 放回的otpOrderNo
            .otpValue(poneCode).build();
            logger.info("出金转账接口请求参数："+obpApiIbankAcctToTransferRequest.toString());
            ObpApiIbankAcctToTransferResponse result = LingShouQuDaoZhuanYongIiLeiHuChongZhiTiXian.create().obpApiIbankAcctToTransfer(obpApiIbankAcctToTransferRequest);
            System.out.println(result);
            logger.info("出金转账接口返回结果："+result);
            return result;
        } catch (ObpApiException e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("出金转账接口异常:"+methodName+"========"+e);
            throw e;
        }
    }

    /**
     * 零售渠道专用II类户充值提现.otp发送接口
     * OTP发送接口，开户接口的otp发送次数控制为1分钟一次，1小时10次。代扣/转账：10秒一次，1小时30次。 支付：30分钟内最多发送10次、验证码有效期2分钟
     *
     */
    public ObpApiIbankCommonSendOtpResponse obpApiIbankCommonSendOtpChuJing(String bno, String scene,String accountName,String fromAccountName,String toAccountNo,String fromAccountNo,String amount,String accountNo,String mobileNo) throws ObpApiException,ObpBeanValidateException{
        try {
            ObpApiIbankCommonSendOtpRequest obpApiIbankCommonSendOtpRequest = ObpApiIbankCommonSendOtpRequest.builder()
            .businessNo(bno)
            .scene(scene)
            .bizKey("{\"accountName\":\""+accountName+"\",\"fromAccountName\":\""+fromAccountName+"\",\"toAccountNo\":\""+toAccountNo+"\",\"fromAccountNo\":\""+fromAccountNo+"\",\"amount\":\""+amount+"\"}")
            .accountNo(accountNo)
            .mobileNo(mobileNo).build();
            logger.info("充值提现otp发送接口请求参数："+obpApiIbankCommonSendOtpRequest.toString());
            ObpApiIbankCommonSendOtpResponse result = LingShouQuDaoZhuanYongIiLeiHuChongZhiTiXian.create().obpApiIbankCommonSendOtp(obpApiIbankCommonSendOtpRequest);
            System.out.println(result);
            logger.info("充值提现otp发送接口返回结果："+result);
            return result;
        } catch (ObpApiException e) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.info("充值提现otp发送接口异常:"+methodName+"========"+e);
            throw e;
        }
    }

}
