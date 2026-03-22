package com.lgyf.demo.dao.impl;

import com.lgyf.demo.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@Repository
public class BaseDaoImpl<T,PK extends Serializable> extends SqlSessionDaoSupport implements BaseDao<T, PK> {
	public static final String SQL_NAME_SEPARATOR=".";
	public static final String SQL_INSERT="insert";
	public static final String SQL_UPDATE="update";
	public static final String SQL_DELETE="delete";
	public static final String SQL_GET_PAGE_LIST="getPageList";
	public static final String SQL_GET_LIST="getList";
	public static final String SQL_GET_ONE="getOne";
	private String sqlNameSpace=this.getNameSpace();

	public String getSqlNameSpace(){
		return sqlNameSpace;
	}

	public void setSqlNameSpace(String sqlNameSpace){
		this.sqlNameSpace=sqlNameSpace;
	}

	public String getNameSpace(){
		Class<T> clazz=null;
		Class c=getClass();
		Type type=c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType=((ParameterizedType)type).getActualTypeArguments();
			clazz=(Class<T>) parameterizedType[0];
			return clazz.getSimpleName();
		}
		return null;
	}

	protected String getSqlName(String sqlName){

		String point = ".";
		if (sqlName.contains(point)) {
			return sqlName;
		}else{
			return sqlNameSpace+SQL_NAME_SEPARATOR+sqlName;
		}
	}


	@Override
	public int insert(String statement, Object parameter){
		return this.getSqlSessionMy().insert(getSqlName(statement), parameter);
	}



	@Override
	public int update(String statement, Object parameter){
		return this.getSqlSessionMy().update(getSqlName(statement), parameter);
	}
	/**
	 * ************************************
	 * 修改-指定SQL语句修改数据(无参数)
	 * @param statement String
	 * @return int
	 * ************************************
	 */


	@Override
	public int delete(String statement, Object parameter){
		return this.getSqlSessionMy().delete(getSqlName(statement),parameter);
	}




	@Override
	public List<Map<String,Object>> selectList(String statement, Object parameter){
		return this.getSqlSessionMy().selectList(getSqlName(statement), parameter);
	}
	@Override
	public List<Map<String,Object>> selectList(String statement){
		return this.getSqlSessionMy().selectList(getSqlName(statement));
	}

	@Override
	public List getObjectList(String statement, Object parameter){
		return this.getSqlSessionMy().selectList(getSqlName(statement),parameter);
	}

	@Override
	public Object selectOne(String statement, Object parameter){
		return this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
	}
	@Override
	public Integer getInt(String statement, Object parameter){
		return (Integer) this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
	}

	@Override
	public Map<String, Object> getOneMap(String mapper, Object para) {
		return this.getSqlSessionMy().selectOne(mapper, para);
	}
	@Override
	public Long getLong(String statement, Object parameter) {
		return (Long) this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
	}

	@Override
	public Double getDouble(String statement, Object parameter){
		Object object=this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
		if(object==null){
			object=0;
		}
		return (Double) this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
	}

	@Override
	public Float getFloat(String statement, Object parameter) {
		return (Float) this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
	}

	/**
	 * ************************************
	 * 统计数量
	 * @param statement String
	 * ************************************
	 */

	@Override
	public Integer getCount(String statement, Object parameter){
		Object o=this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
		if(o==null){
			o=0;
		}
		return (Integer) o;
	}


	@Override
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	protected <S> S getMapper(Class<S> clazz) {
		return getSqlSession().getMapper(clazz);
	}

	@Override
	public String getString(String statement, Object parameter){
		return (String) this.getSqlSessionMy().selectOne(getSqlName(statement), parameter);
	}

	public SqlSession getSqlSessionMy(){
		this.getSqlSessionFactory().getConfiguration().setMapUnderscoreToCamelCase(false);
		return this.getSqlSession();
	}
	/**
	 * 获取一条记录
	 */
	public Object getObject(String statement) {
		return this.getSqlSessionMy().selectOne(getSqlName(statement));
	}

	public Object getObject(String statement, Object parameter) {
		return this.getSqlSessionMy().selectOne(getSqlName(statement),parameter);
	}

}
