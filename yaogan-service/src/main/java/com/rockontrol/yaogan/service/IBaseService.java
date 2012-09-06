package com.rockontrol.yaogan.service;

public interface IBaseService {

   public void save(Object obj);

   public void saveOrupdate(Object obj);

   public <T> T getByID(Class<T> clazz, Long id);
}
