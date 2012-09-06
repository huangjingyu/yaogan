package com.rockontrol.yaogan.util;

public class Pair<T, TT> {
   private T first;
   private TT second;

   public Pair() {
   }

   public Pair(T first, TT second) {
      this.first = first;
      this.second = second;
   }

   public T getFirst() {
      return first;
   }

   public void setFirst(T first) {
      this.first = first;
   }

   public TT getSecond() {
      return second;
   }

   public void setSecond(TT second) {
      this.second = second;
   }

}
