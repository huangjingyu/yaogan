package com.rockontrol.yaogan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity(name = "user_place")
public class UserPlace {
   private Long id;
   private Long userId;
   private Long placeId;
   private User user;
   private Place place;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   @Column(name = "user_id")
   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   @Column(name = "place_id")
   public Long getPlaceId() {
      return placeId;
   }

   public void setPlaceId(Long placeId) {
      this.placeId = placeId;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", insertable = false, updatable = false)
   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "place_id", insertable = false, updatable = false)
   public Place getPlace() {
      return place;
   }

   public void setPlace(Place place) {
      this.place = place;
   }

}
