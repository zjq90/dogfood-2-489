package com.lgyf.demo.dao.impl;

import com.lgyf.demo.bean.EleAccount;
import com.lgyf.demo.dao.EleAccountDao;
import org.springframework.stereotype.Repository;

@Repository
public class EleAccountDaoImpl extends BaseDaoImpl<EleAccount, Integer> implements EleAccountDao {

    @Override
    public int insertEleAccount(EleAccount eleAccount) {
        return this.insert("EleAccount.insertEleAccount", eleAccount);
    }

    @Override
    public EleAccount getByCertNo(String certNo) {
        return (EleAccount) this.selectOne("EleAccount.getByCertNo", certNo);
    }

    @Override
    public EleAccount getByEleAccountNo(String eleAccountNo) {
        return (EleAccount) this.selectOne("EleAccount.getByEleAccountNo", eleAccountNo);
    }
}
