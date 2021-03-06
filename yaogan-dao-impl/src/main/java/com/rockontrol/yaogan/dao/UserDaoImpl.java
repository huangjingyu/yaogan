package com.rockontrol.yaogan.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rockontrol.yaogan.model.User;

@Repository("user_PlaceDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {

   public User getUserByName(String userName) {
      DetachedCriteria criteria = DetachedCriteria.forClass(User.class).add(
            Restrictions.eq("userName", userName));
      User user = (User) criteria.getExecutableCriteria(getSession()).uniqueResult();
      return user;
   }
}
