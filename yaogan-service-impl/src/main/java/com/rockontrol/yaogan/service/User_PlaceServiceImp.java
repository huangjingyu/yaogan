package com.rockontrol.yaogan.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rockontrol.yaogan.dao.User_PlaceDaoIml;
import com.rockontrol.yaogan.model.UserPlace;
@Service("user_PlaceServiceImp")
public class User_PlaceServiceImp implements User_PlaceService {

  private User_PlaceDaoIml user_PlaceDaoIml ;

   public User_PlaceDaoIml getUser_PlaceDaoIml() {
   return user_PlaceDaoIml;
}
@Resource(name="user_PlaceDao")
public void setUser_PlaceDaoIml(User_PlaceDaoIml user_PlaceDaoIml) {
   this.user_PlaceDaoIml = user_PlaceDaoIml;
}

public void insertUser_place(UserPlace userPlace) {
   // TODO Auto-generated method stub
   System.out.println("进入service了");
   user_PlaceDaoIml.insertUser_place(userPlace);
   
}


   

}
