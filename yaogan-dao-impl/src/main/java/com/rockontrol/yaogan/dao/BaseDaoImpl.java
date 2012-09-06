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
   public List<T> findByPage(String hql, int startIndex, int maxCount, Object[] params) {
      Query query = this.getSession().createQuery(hql);
      if (params != null && params.length > 0) {
         for (int index = 0; index < params.length; index++) {
            query.setParameter(index, params[index]);
         }
      }

      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

   @Override
   public List<T> findByHQL(String hql, Object[] params) {
      Query query = this.getSession().createQuery(hql);
      if (params != null && params.length > 0) {
         for (int index = 0; index < params.length; index++) {
            query.setParameter(index, params[index]);
         }
      }
      return query.list();
   }

   @Override
   public List<T> findByPage(String hql, int startIndex, int maxCount,
         Map<String, Object> map) {
      Query query = this.getSession().createQuery(hql);
      if (map != null && map.size() > 0) {
         Set<Map.Entry<String, Object>> set = map.entrySet();
         for (Entry<String, Object> entry : set) {
            query.setParameter(entry.getKey(), entry.getValue());
         }
      }
      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

}
