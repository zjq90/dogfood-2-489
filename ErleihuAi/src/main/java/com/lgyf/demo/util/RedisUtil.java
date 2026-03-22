package com.lgyf.demo.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger("timer");

    private static Logger error = LoggerFactory.getLogger("error");
    //https://blog.csdn.net/lydms/article/details/105224210

    /**
     * redisTemplate 序列化使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Resource
    private RedisTemplate redisTemplate;

    //删除单个key
    public void delede(String key){
        redisTemplate.delete(key);
    }

    //删除多个key
    public void deleteKey (String ...keys){
        redisTemplate.delete(keys);
    }

    //放入String
    public void putString(String key,String value){
        try {
            redisTemplate.boundValueOps(key).set(value);
        }catch (Exception e){
            logger.info("redis put err key ["+key+"]:"+e.getMessage().toString());
            error.info("redis put err key:"+key);
        }
    }

    //放入String
    public void putString(String key,String value,int num){
        redisTemplate.boundValueOps(key).set(value,1, TimeUnit.MINUTES);
    }

    public void putStringS(String key,String value,int num) {
        redisTemplate.boundValueOps(key).set(value,(long)num, TimeUnit.SECONDS);
    }

    /**
     * 检测并且存在则写入 防止并发
     * @param key
     * @param value
     * @param num 分钟
     * @return 是否存在
     */
    public synchronized boolean checkPutString(String key,String value,int num){
        if(getString(key)!=null){
            return true;
        }
        putString(key,value,num);
        return false;
    }

    /**
     * 检测并且写入实体（秒级）
     * @param key
     * @param value
     * @return
     */
    public synchronized boolean checkPutObjectS(String key,Object value,int num){
        if(getString(key)!=null){
            return true;
        }
        String value_str= JSONObject.fromObject(value).toString();
        putStringS(key,value_str,num);
        return false;
    }

    public synchronized void  putObjectS(String key,Object value,int num) {
        String value_str= JSONObject.fromObject(value).toString();
        putStringS(key,value_str,num);
    }

    public synchronized void putObject(String key,Object value,int num){
        String value_str= JSONObject.fromObject(value).toString();
        putString(key,value_str,num);
    }

    /**
     * 检测并且写入实体（分级）
     * @param key
     * @param value
     * @return
     */
    public synchronized boolean checkPutObject(String key,Object value,int num){
        if(getString(key)!=null){
            return true;
        }
        String value_str= JSONObject.fromObject(value).toString();
        putString(key,value_str,num);
        return false;
    }

    /**
     * 检测并且存在则写入,防止并发
     * @param key
     * @param value
     * @param num 秒
     * @return 是否存在
     */
    public synchronized boolean checkPutStringS(String key,String value,int num){
        if(getString(key)!=null){
            return true;
        }
        putStringS(key,value,num);
        return false;
    }

    public String getString(String key){
        String result="";
            try {
                result=redisTemplate.boundValueOps(key).get()==null?null:redisTemplate.boundValueOps(key).get().toString();
                return result;
            }catch (Exception e){
                logger.info("redis put err key ["+key+"]:"+e.getMessage().toString());
                error.info("redis put err key:"+key);
                return null;
            }
    }

    public void putMap(String key, Map map){
        redisTemplate.boundHashOps(key).putAll(map);
    }

    public Map<String,Object> getMap(String key){
        return (Map<String,Object>)redisTemplate.boundHashOps(key).entries();
    }

    public synchronized <T> T getObject(String key, Class<T> clazz){
        String tsar=getString(key);
        if(tsar==null){
            return null;
        }
        T t= (T) JSON.parseObject(tsar,clazz);
        return t;
    }


    public Object getValue(String key,String small_key){
        return redisTemplate.boundHashOps(key).get(small_key);
    }

    public boolean hasKey(String key,String small_key){
        return redisTemplate.boundHashOps(key).hasKey(small_key);
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public void putList(String key,List list){
        redisTemplate.boundListOps(key).leftPushAll(list);
    }

    public List getList(String key,int start,int end){
        return redisTemplate.boundListOps(key).range(start, end);
    }

    public void zset(String key,double score,String value){
        redisTemplate.boundZSetOps(key).add(value,score);
    }

    public Set<String> getZSet(String key){
        return redisTemplate.boundZSetOps(key).range(0, -1);
    }

    public Double getScore(String key,String value){
        return redisTemplate.boundZSetOps(key).score(value);
    }

    public void removeZSet(String key,String value){
        redisTemplate.boundZSetOps(key).remove(value);
    }

    public void addScore(String key,String value,double socre){
        redisTemplate.boundZSetOps(key).incrementScore(value,socre);
    }
}
