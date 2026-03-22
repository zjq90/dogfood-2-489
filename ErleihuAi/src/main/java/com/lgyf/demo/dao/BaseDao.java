package com.lgyf.demo.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface BaseDao<T,PK extends Serializable> {

	int insert(String statement, Object parameter);
	int update(String statement, Object parameter);

	int delete(String statement, Object parameter);

	List<Map<String,Object>> selectList(String statement, Object parameter);

	List<Map<String,Object>> selectList(String statement);

	List getObjectList(String statement, Object parameter);

	Map<String,Object> getOneMap(String statement, Object par);

	Object selectOne(String statement, Object parameter);

	Integer getInt(String statement, Object parameter);

	Long getLong(String statement, Object parameter);

	Double getDouble(String statement, Object parameter);

	Float getFloat(String statement, Object parameter);

	Integer getCount(String statement, Object parameter);

	String getString(String statement, Object parameter);
	Object getObject(String statement);
	Object getObject(String statement, Object parameter);
}
