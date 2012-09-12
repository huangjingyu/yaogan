package com.rockontrol.yaogan.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

public interface IBaseDao<T> {
   public T get(Long id);

   public T load(Long id);

   public List<T> findAll();

   public void save(T entity);

   public void update(T entity);

   public void delete(T entity);

   public void deleteById(Long id);

   /**
    * 分页查询
    * 
    * @param hql
    *           hql语句
    * @param startIndex
    *           起始序号 0
    * @param maxCount
    *           最大条数
    * @param params
    *           查询参数数组，若无查询参数填空或长度为0的空数组
    * 
    * @return
    */
   public List findByPage(String hql, int startIndex, int maxCount, Object[] params);

   public long getCount(String hql, Object[] params);

   /**
    * 
    * @param hql
    * @param startIndex
    *           numbered from 0
    * @param maxCount
    * @param map
    * @return
    */
   public List findByPage(String hql, int startIndex, int maxCount,
         Map<String, Object> map);

   public long getCount(String hql, Map<String, Object> map);

   /**
    * 
    * @param hql
    * @param params
    * @return
    */
   public List findByHQL(String hql, Object[] params);

   /**
    * 
    * @param query
    * @param startIndex
    *           numbered from 0
    * @param maxCount
    * @param params
    * @return
    */
   public List findByPage(Query query, int startIndex, int maxCount, Object[] params);

   public long getCount(Query query, Object[] params);

   /**
    * 
    * @param query
    * @param startIndex
    *           numbered from 0
    * @param maxCount
    * @param map
    * @return
    */
   public List findByPage(Query query, int startIndex, int maxCount,
         Map<String, Object> map);

   public long getCount(Query query, Map<String, Object> map);
}
