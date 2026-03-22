package com.lgyf.demo.dao.impl;

import com.lgyf.demo.bean.OpenAccount;
import com.lgyf.demo.dao.OpenAccountDao;
import org.springframework.stereotype.Repository;

@Repository
public class OpenAccountDaoImpl extends BaseDaoImpl<OpenAccount, Integer> implements OpenAccountDao {

    @Override
    public OpenAccount getByCertNo(String certNo) {
        return (OpenAccount) this.selectOne("OpenAccount.getByCertNo", certNo);
    }

    @Override
    public int insertOpenAccount(OpenAccount openAccount) {
        return this.insert("OpenAccount.insertOpenAccount", openAccount);
    }

    @Override
    public int updateOpenStatus(OpenAccount openAccount) {
        return this.update("OpenAccount.updateOpenStatus", openAccount);
    }

    @Override
    public OpenAccount getByBusinessNo(String businessNo) {
        return (OpenAccount) this.selectOne("OpenAccount.getByBusinessNo", businessNo);
    }
}
