package com.lgyf.demo.util;

import java.util.Map;

/***********************************************
 * @Author merry$
 * @Description //TODO $
 * @Date $ $
 * @Param $
 * @return $
 ***********************************************/
public class PayUtils {
    /**
     * 获取map中int
     * @param map
     * @param key
     * @return
     */
      public static Integer getMapIntArg(Map map, String key){
            if(!map.containsKey(key)){ return 0;}
            if(map.get(key)==null){return 0;}
            if(!map.get(key).toString().matches("^[0-9]+[\\.]?[0-9]*$")){return 0;}
            return Double.valueOf(map.get(key).toString()).intValue();
      }

      public static String getMapStrArg(Map map,String key){
           if(!map.containsKey(key)){return "";}
           if(map.get(key)==null){return "";}
           return map.get(key).toString().trim();
      }

      public static Double getStrDouble(String str){
          System.out.println("转换前str---"+str);
            if(str==null){ return Double.valueOf(0);}
            return Double.valueOf(str);
      }

      public static Integer getStrInt(String str){
          System.out.println("转换前int---"+str);
            if(str==null){
                return 0;
            }
            return Integer.parseInt(str);
      }
      
      public static String getTradeTypeCn(String trade_type){
            switch (trade_type){
                case "0":
                    return "刷卡";
                case "1":
                    return "支付宝";
                case "2":
                    return "微信";
                case "3":
                    return "云闪付";
                case "4":
                    return "储蓄卡刷卡";
                case "5":
                    return "押金";
                case "6":
                    return "优选付贷记卡";
                case "7":
                    return "快捷";
                case "8":
                    return "畅享特惠借记卡";
                case "9":
                    return "畅享特惠贷记卡";
                case "10":
                    return "优选付借记卡";
                default:
                    return "其它";
            }
          
      }

}
