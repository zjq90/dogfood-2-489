package com.lgyf.demo;

import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class DemoApplicationTests {

//    @Test
//    public void testString() {
//        //1.连接redis
//        Jedis jedis = new RedisUtil().getJedis();
//        //2.操作redis
////        jedis.set("jedis01","caozuoredis");
//        System.out.println(jedis.get("jedis01"));
//        //3.关闭连接
////        jedis.close();
//    }
//
//    @Test
//    public void testList() {
//        //1.连接redis
////        Jedis jedis = new Jedis("192.168.198.250", 6379);
//        Jedis jedis= JedisUtil.getJedis();
//        //2.操作redis
//        List<String> list1 = jedis.lrange("list1", 0, -1);
//        for (String str:list1){
//            System.out.println(str);
//        }
//        //3.关闭连接
//        jedis.close();
//    }
//
//    @Test
//    public void testHash() {
//        //1.连接redis
//        Jedis jedis = new Jedis("192.168.198.250", 6379);
//        //2.操作redis
//        List<String> qqq = jedis.hvals("qqq");
//        for (String str:qqq){
//            System.out.println(str);
//        }
////        Map<String, String> qqq1 = jedis.hgetAll("qqq");
//        //3.关闭连接
//        jedis.close();
//    }
}
