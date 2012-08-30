package com.rockontrol.yaogan.model;

import javax.persistence.Entity;

@Entity(name = "shapefile")
public class Shapefile {
   private Long _id;
   private String _fileName;
   private String _WMSURL;
   private String _shootTime;
}
