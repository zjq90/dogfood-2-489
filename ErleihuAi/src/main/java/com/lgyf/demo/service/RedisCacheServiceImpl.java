package com.lgyf.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lgyf.demo.bean.AgentArgs;
import com.lgyf.demo.bean.Client;
import com.lgyf.demo.dao.ClientDao;
import com.lgyf.demo.util.RedisUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@EnableScheduling
@Service
public class RedisCacheServiceImpl implements RedisCacheService {
    private static Logger logger = LoggerFactory.getLogger("INFO_FILE");
    private static Logger error = LoggerFactory.getLogger("ERROR_FILE");

    @Resource
    private ClientDao clientDao;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 定时更新缓存参数
     */
   @Scheduled(fixedRate = 1000*60*10)
    public  void timerClient(){
       System.out.println("执行CLIENT缓存更新--");
       //List<Client2> listClient=clientDao.getObjectList("clientList","");
       List<Client> listClient=clientDao.getObjectList("clientList","");
       for (Client client:listClient) {
           redisUtil.putString("ERLEIHU:CLIENT:"+client.getClient_no(),JSON.toJSONString(client,SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
       }
       List<AgentArgs> listAgent=clientDao.getObjectList("agentArgsList","");
       for (AgentArgs agentArgs:listAgent) {
           redisUtil.putString("ERLEIHU:AGENTARGS:"+agentArgs.getAgent_no(),JSON.toJSONString(agentArgs,SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
       }
    }

    /**
     * 获取商户信息
     * @param client_no
     * @return
     */
    public Client getClient(String client_no){
        Client client=null;
        try{
            String ct_str=redisUtil.getString("ERLEIHU:CLIENT:"+client_no);
            JSONObject jsonObject=JSONObject.fromObject(ct_str);
            client=(Client) JSONObject.toBean(jsonObject, Client.class);
            if(client==null){
                client= (Client) clientDao.selectOne("getClientByNo",client_no);
            }
            return client;
        }catch (Exception e){  //redis 出错的情况下
            client= (Client) clientDao.selectOne("getClientByNo",client_no);
            return client;
        }

    }

    /**
     * 获取渠道参数
     * @param agent_no
     * @return
     */
    public AgentArgs getAgentArgs(String agent_no){
        AgentArgs agentArgs=null;
        try{
            String ct_str=redisUtil.getString("ERLEIHU:AGENTARGS:"+agent_no);
            JSONObject jsonObject=JSONObject.fromObject(ct_str);
            agentArgs=(AgentArgs) JSONObject.toBean(jsonObject, AgentArgs.class);
            if(agentArgs==null){
                agentArgs= (AgentArgs) clientDao.selectOne("getAgentArgsByNo",agent_no);
            }
            return agentArgs;
        }catch (Exception e){  //redis 出错的情况下
            agentArgs= (AgentArgs) clientDao.selectOne("getAgentArgsByNo",agent_no);
            return agentArgs;
        }
    }




}
