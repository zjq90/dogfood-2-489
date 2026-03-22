package com.lgyf.demo.dao.impl;

import com.lgyf.demo.bean.OpenAccount;
import com.lgyf.demo.dao.OpenAccountDao;
import org.springframework.stereotype.Repository;

/**
 * 开户数据访问实现类
 * 继承BaseDaoImpl，实现OpenAccountDao接口
 * 提供开户记录和电子账户的具体数据库操作实现
 */
@Repository
public class OpenAccountDaoImpl extends BaseDaoImpl<OpenAccount, Integer> implements OpenAccountDao{
}
