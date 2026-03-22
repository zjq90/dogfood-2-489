package com.lgyf.demo.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 易购商城自定义响应结构
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }

    /**
     * 将json结果集转化为对象
     *clazz 对象中的object类型
     * @param jsonData json数据
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }

    public static void main(String[] args) {
        String str = "{\"test\":\"test\",\"wechat\":\"1\",\"pay\":\"2\",\"d0\":\"3\",\"d1\":\"4\",\"T1\":\"6\",\"d2\":\"5\"}";
        Map map = JSON.parseObject(str,Map.class);
        for(Object s:map.keySet()){
            System.out.println("key : "+s+" value : "+map.get(s));
        }
    }
    
}
