package com.lgyf.demo.util;

import java.util.regex.Pattern;

/***********************************************
 * @Author merry$
 * @Description  TEST
 * @Date $ $
 * @Param $
 * @return $
 ***********************************************/
public class testReg {

    public static void main(String[] args){
        String pattern="^C[0-9]{4}/g";
        String content="C00010";
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("结果-------------------"+isMatch);
    }

}
