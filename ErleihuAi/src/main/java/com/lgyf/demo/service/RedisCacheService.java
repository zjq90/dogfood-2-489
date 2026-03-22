package com.lgyf.demo.service;

import com.lgyf.demo.bean.AgentArgs;
import com.lgyf.demo.bean.Client;
import org.springframework.stereotype.Service;


@Service
public interface RedisCacheService {

    public  void timerClient();
    public Client getClient(String client_no);
    public AgentArgs getAgentArgs(String agent_no);


}
