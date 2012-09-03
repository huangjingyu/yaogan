package com.rockontrol.yaogan.dao;

public interface IBaseDao<T> {
   public T get(Long id);
   
   public void save(T entity);
}
