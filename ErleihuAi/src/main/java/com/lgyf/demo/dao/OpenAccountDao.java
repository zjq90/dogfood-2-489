package com.lgyf.demo.dao;

import com.lgyf.demo.bean.OpenAccount;

public interface OpenAccountDao extends BaseDao<OpenAccount, Integer> {
    
    /**
     * 根据身份证号查询开户信息
     * @param certNo 身份证号
     * @return 开户信息
     */
    OpenAccount getByCertNo(String certNo);
    
    /**
     * 插入开户信息
     * @param openAccount 开户信息
     * @return 影响行数
     */
    int insertOpenAccount(OpenAccount openAccount);
    
    /**
     * 更新开户状态
     * @param openAccount 开户信息
     * @return 影响行数
     */
    int updateOpenStatus(OpenAccount openAccount);
    
    /**
     * 根据业务号查询开户记录
     * @param businessNo 业务号
     * @return 开户信息
     */
    OpenAccount getByBusinessNo(String businessNo);
}
