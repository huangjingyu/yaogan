package com.rockontrol.yaogan.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.rockontrol.yaogan.dao.IBaseDao;

public class BaseServiceImpl implements IBaseService {
   @Autowired
   protected IBaseDao baseDao;

   @Override
   public void save(Object obj) {
      baseDao.save(obj);
   }

   @Override
   public void saveOrupdate(Object obj) {

   }

   @Override
   public <T> T getByID(Class<T> clazz, Long id) {
      baseDao.get(id);
      return null;
   }

}
