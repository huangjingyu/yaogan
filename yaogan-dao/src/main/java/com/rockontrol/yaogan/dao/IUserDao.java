package com.rockontrol.yaogan.dao;

import com.rockontrol.yaogan.model.User;

public interface IUserDao extends IBaseDao<User> {
   User getUserByName(String userName);
}
