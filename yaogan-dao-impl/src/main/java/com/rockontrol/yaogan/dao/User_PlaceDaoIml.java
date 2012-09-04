package com.rockontrol.yaogan.dao;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rockontrol.yaogan.model.UserPlace;
@Component("user_PlaceDao")
public class User_PlaceDaoIml implements User_PlaceDao {
   private SessionFactory sessionFactory;
  @Resource(name="sessionFactory")
   public SessionFactory getSessionFactory() {
      return sessionFactory;
   }

   public void setSessionFactory(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   @Override
   public void insertUser_place(UserPlace userPlace) {
    
        Session session=sessionFactory.openSession();
        try{
        session.beginTransaction();
        session.save(userPlace);
        }
        catch(Exception e){
           e.printStackTrace();
           
        }
        finally{
           session.getTransaction().commit();
        }
     // System.out.println("进入dao了");
   }

}
