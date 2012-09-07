package com.rockontrol.yaogan.vo;

public class EnvStats {
   // 生物丰度指数
   private double abio;

   // 植被覆盖指数
   private double aveg;

   // 土地退化指数
   private double aero;

   // 土地环境指数
   private double asus;

   public EnvStats() {
   }

   public EnvStats(double abio, double aveg, double aero, double asus) {
      this.abio = abio;
      this.aveg = aveg;
      this.aero = aero;
      this.asus = asus;
   }

   public double getAbio() {
      return abio;
   }

   public void setAbio(double abio) {
      this.abio = abio;
   }

   public double getAveg() {
      return aveg;
   }

   public void setAveg(double aveg) {
      this.aveg = aveg;
   }

   public double getAero() {
      return aero;
   }

   public void setAero(double aero) {
      this.aero = aero;
   }

   public double getAsus() {
      return asus;
   }

   public void setAsus(double asus) {
      this.asus = asus;
   }

}
