package com.lgyf.demo.dao.impl;

import com.lgyf.demo.bean.Client;
import com.lgyf.demo.dao.ClientDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
@Repository
public class ClientDaoImpl extends BaseDaoImpl<Client, Integer> implements ClientDao{
}
