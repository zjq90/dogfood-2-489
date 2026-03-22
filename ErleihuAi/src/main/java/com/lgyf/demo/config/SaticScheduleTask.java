package com.lgyf.demo.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;


@EnableScheduling   // 2.开启定时任务
@Component
public class SaticScheduleTask {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SaticScheduleTask.class);


   // @Scheduled(fixedRate=300000)
    private void configureTasks() throws ParseException {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = df.format(new Date());
//        Date d1  = df.parse(date);
//        List<Map<String, Object>> mapList = zfbPayDao.selectList("taskSelect");
//        for (Map<String, Object> map : mapList) {
//             Object dateStr = map.get("add_time");
//            Date d2 = df.parse(String.valueOf(dateStr));
//            long diff = d1.getTime() - d2.getTime();
//            long mitime = diff / (1000 * 60 * 30);
//            if(mitime>30){
//                zfbPayDao.update("updateISouttime",map.get("out_trade_no"));
//                logger.info("修改支付超时订单,流水号："+map.get("out_trade_no")+"支付超时");
//            }
//        }


        System.out.println("执行静态定时任务时间: " + LocalDateTime.now());
    }

}
