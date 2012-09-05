package com.rockontrol.yaogan.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

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
   public List<T> findAll() {
      Query query = getSession().createQuery("from " + entityClass.getName());
      return query.list();
   }

   @Override
   public void save(T entity) {
      getSession().save(entity);
   }

   @Override
   public void remove(T entity) {
      getSession().delete(entity);
   }

}
