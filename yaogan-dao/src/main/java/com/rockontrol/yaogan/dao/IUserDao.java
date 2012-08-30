package com.rockontrol.yaogan.dao;

import com.rockontrol.yaogan.model.User;

public interface IUserDao {
   User getUserByName(String userName);
}
