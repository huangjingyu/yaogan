package com.rockontrol.yaogan.dao;

import java.util.List;

public interface IBaseDao<T> {
   public T get(Long id);

   public List<T> findAll();

   public void save(T entity);
}
