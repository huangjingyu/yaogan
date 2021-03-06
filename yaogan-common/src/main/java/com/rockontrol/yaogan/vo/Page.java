package com.rockontrol.yaogan.vo;

import java.util.List;

public class Page<T> {
   private List<T> items;
   private int curPageNum; // start from 1 ，当前页数
   private int totalPageNum; // 总页数
   private long totalItemNum; // 总条目数
   private int pageSize; // 每页大小
   private int startItemIndex; // 每页开始的条目是总的第几条

   public Page(int curPageNum, int pageSize) {
      this.curPageNum = curPageNum;
      this.pageSize = pageSize;
      this.startItemIndex = (curPageNum - 1) * pageSize;
   }

   public List<T> getItems() {
      return items;
   }

   public void setItems(List<T> items) {
      this.items = items;
   }

   public int getCurPageNum() {
      return curPageNum;
   }

   public int getTotalPageNum() {
      return totalPageNum;
   }

   public long getTotalItemNum() {
      return totalItemNum;
   }

   public void setTotalItemNum(long totalItemNum) {
      this.totalItemNum = totalItemNum;
      int result = (int) (totalItemNum / pageSize);
      long remainder = totalItemNum % pageSize;
      if (remainder == 0) {
         this.totalPageNum = result;
      } else {
         this.totalPageNum = result + 1;
      }
   }

   public int getPageSize() {
      return pageSize;
   }

   public int getStartItemIndex() {
      return startItemIndex;
   }

}
