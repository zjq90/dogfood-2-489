package com.lgyf.demo.dao;

import com.lgyf.demo.bean.EleAccount;

public interface EleAccountDao extends BaseDao<EleAccount, Integer> {
    
    /**
     * 插入电子账户信息
     * @param eleAccount 电子账户信息
     * @return 影响行数
     */
    int insertEleAccount(EleAccount eleAccount);
    
    /**
     * 根据身份证号查询电子账户信息
     * @param certNo 身份证号
     * @return 电子账户信息
     */
    EleAccount getByCertNo(String certNo);
    
    /**
     * 根据电子账号查询
     * @param eleAccountNo 电子账号
     * @return 电子账户信息
     */
    EleAccount getByEleAccountNo(String eleAccountNo);
}
