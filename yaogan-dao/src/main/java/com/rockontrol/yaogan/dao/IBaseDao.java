package com.rockontrol.yaogan.dao;

import java.util.List;
import java.util.Map;

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
    *           起始序号
    * @param maxCount
    *           最大条数
    * @param params
    *           查询参数数组，若无查询参数填空或长度为0的空数组
    * 
    * @return
    */
   public List<T> findByPage(String hql, int startIndex, int maxCount, Object[] params);

   /**
    * 
    * @param hql
    * @param startIndex
    * @param maxCount
    * @param map
    * @return
    */
   public List<T> findByPage(String hql, int startIndex, int maxCount,
         Map<String, Object> map);

   /**
    * 
    * @param hql
    * @param params
    * @return
    */
   public List<T> findByHQL(String hql, Object[] params);
}
