package com.rockontrol.yaogan.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDaoImpl<T> implements IBaseDao<T> {
   private final Class<T> entityClass;

   @Autowired
   private SessionFactory sessionFactory;

   @SuppressWarnings("unchecked")
   public BaseDaoImpl() {
      entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];
   }

   protected Session getSession() {
      return sessionFactory.getCurrentSession();
   }

   @SuppressWarnings("unchecked")
   @Override
   public T get(Long id) {
      return (T) getSession().get(entityClass, id);
   }

   @SuppressWarnings("unchecked")
   @Override
   public T load(Long id) {
      return (T) getSession().load(entityClass, id);
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<T> findAll() {
      Query query = getSession().createQuery("from " + entityClass.getName());
      return query.list();
   }

   @Override
   public void save(T entity) {
      getSession().save(entity);
   }

   @Override
   public void delete(T entity) {
      getSession().delete(entity);
   }

   @Override
   public void deleteById(Long id) {
      T obj = load(id);
      delete(obj);
   }

   @Override
   public void update(T entity) {
      getSession().update(entity);
   }

   @Override
   public List findByPage(String hql, int startIndex, int maxCount, Object[] params) {
      return findByPage(getSession().createQuery(hql), startIndex, maxCount, params);
   }

   @Override
   public List findByHQL(String hql, Object[] params) {
      Query query = this.getSession().createQuery(hql);
      if (params != null && params.length > 0) {
         for (int index = 0; index < params.length; index++) {
            query.setParameter(index, params[index]);
         }
      }
      return query.list();
   }

   @Override
   public List findByPage(String hql, int startIndex, int maxCount,
         Map<String, Object> map) {
      return findByPage(getSession().createQuery(hql), startIndex, maxCount, map);
   }

   @Override
   public List findByPage(Query query, int startIndex, int maxCount, Object[] params) {
      _setParams(query, params);
      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

   @Override
   public List findByPage(Query query, int startIndex, int maxCount,
         Map<String, Object> map) {
      _setParams(query, map);
      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

   @Override
   public long getCount(String hql, Object[] params) {
      return getCount(getSession().createQuery("select count(t) from (" + hql + ") t"),
            params);
   }

   @Override
   public long getCount(String hql, Map<String, Object> map) {
      return getCount(getSession().createQuery("select count(*) from (" + hql + ") t"),
            map);
   }

   @Override
   public long getCount(Query query, Object[] params) {
      _setParams(query, params);
      return ((Long) query.uniqueResult()).longValue();
   }

   @Override
   public long getCount(Query query, Map<String, Object> map) {
      _setParams(query, map);
      return ((Long) query.uniqueResult()).longValue();
   }

   private void _setParams(Query query, Object[] params) {
      if (params != null && params.length > 0) {
         for (int index = 0; index < params.length; index++) {
            query.setParameter(index, params[index]);
         }
      }
   }

   private void _setParams(Query query, Map<String, Object> map) {
      if (map != null && map.size() > 0) {
         Set<Map.Entry<String, Object>> set = map.entrySet();
         for (Entry<String, Object> entry : set) {
            query.setParameter(entry.getKey(), entry.getValue());
         }
      }
   }

}
