package com.lgyf.demo.util;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;

/**
 * @PROJECT_NAME: water_chivalry
 * @AUTHOR: Hanson-Hsc
 * @DATE: 2020-07-27 09:08
 * @DESCRIPTION: 图片压缩工具
 * @VERSION:
 */
@Component
public class PicUtils {
    //以下是常量,按照阿里代码开发规范,不允许代码中出现魔法值
    private static final Logger logger = LoggerFactory.getLogger(PicUtils.class);
    private static final Integer ZERO = 0;
    private static final Integer ONE_ZERO_TWO_FOUR = 1024;
    private static final Integer NINE_ZERO_ZERO = 900;
    private static final Integer THREE_TWO_SEVEN_FIVE = 3275;
    private static final Integer TWO_ZERO_FOUR_SEVEN = 2047;
    private static final Double ZERO_EIGHT_FIVE = 0.85;
    private static final Double ZERO_SIX = 0.6;
    private static final Double ZERO_FOUR_FOUR = 0.44;
    private static final Double ZERO_FOUR = 0.4;

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) {
        if (imageBytes == null || imageBytes.length <= ZERO || imageBytes.length < desFileSize * ONE_ZERO_TWO_FOUR) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / ONE_ZERO_TWO_FOUR);
        try {
            while (imageBytes.length > desFileSize * ONE_ZERO_TWO_FOUR) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            logger.info("图片原大小={}kb | 压缩后大小={}kb",
                    srcSize / ONE_ZERO_TWO_FOUR, imageBytes.length / ONE_ZERO_TWO_FOUR);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < NINE_ZERO_ZERO) {
            accuracy = ZERO_EIGHT_FIVE;
        } else if (size < TWO_ZERO_FOUR_SEVEN) {
            accuracy = ZERO_SIX;
        } else if (size < THREE_TWO_SEVEN_FIVE) {
            accuracy = ZERO_FOUR_FOUR;
        } else {
            accuracy = ZERO_FOUR;
        }
        return accuracy;
    }

    /**
     * 图片压缩后转byte[]再转16进制HEX
     * @param imgUrl  图片路径
     * @param imgCmpSize  压缩到小于多少KB
     * @return
     */
    public static String img2hex(String imgUrl,Integer imgCmpSize){
        if(!imgUrl.equals("")){
            byte[] imgByte=PicUtils.compressPicForScale(PicUtils.image2byte(imgUrl),imgCmpSize); //压缩并且转为HEX
            return PicUtils.byte2hex(imgByte);
        }
        return "";
    }
    //传byte数组
    public static byte[]  img2hexdata(byte[] imgUrl,Integer imgCmpSize){
        if(!imgUrl.equals("")){
            byte[] imgByte=PicUtils.compressPicForScale(imgUrl,imgCmpSize); //压缩并且转为HEX
            return imgByte;
        }
        return null;
    }
    /**
     * 图片转byte
     * @param path
     * @return
     */
    public static byte[] image2byte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }
    //byte数组到图片
    public static boolean byte2image(byte[] data,String path){
        System.out.println();
        if(data.length<3||path.equals("")) return false;
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();

            System.out.println("Make Picture success,Please find image in " + path);
            return true;
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
            return false;
        }
    }
    //byte数组到16进制字符串
    public static String byte2hex(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * hex转byte数组
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex){
        int m = 0, n = 0;
        int byteLen = hex.length() / 2; // 每两个字符描述一个字节
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = Byte.valueOf((byte)intVal);
        }
        return ret;
    }

    /**
     * 云服根据手机号判断身份证是否存在
     */
    public static String checkImg(String imgUrl,String mobile,String zf){
        if(!imgUrl.endsWith("/")){
            imgUrl+="/";
        }
        String houzhui=".jpg";
        String sfz=imgUrl+mobile+"_"+zf+houzhui;
        File z=new File(sfz);
        if(z.exists()){
            return sfz;
        }
        houzhui=".png";
        sfz=imgUrl+mobile+"_"+zf+houzhui;
        z=new File(sfz);
        if(z.exists()){
            return sfz;
        }
        return "";
    }

    public static void main(String[] args) {
//        System.out.println("============");
//        File ff = new File("D:\\java","11213.txt");
//        if(!ff.exists()){
//            try {
//                ff.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//            System.out.println(ff);
        String s = PicUtils.img2hex("C:\\image\\2_0.jpg", 512);
        System.out.println(s);
    }

}