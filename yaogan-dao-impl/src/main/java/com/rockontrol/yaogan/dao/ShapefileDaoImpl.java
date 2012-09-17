package com.rockontrol.yaogan.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.User;

@Repository("shapefileDao")
public class ShapefileDaoImpl extends BaseDaoImpl<Shapefile> implements IShapefileDao {

   @SuppressWarnings("unchecked")
   @Override
   public List<String> getAvailableTimesOfPlace(Long placeId) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailableTimesOfPlace");
      query.setLong("placeId", placeId);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<Place> getAvailablePlacesOfUser(Long userId, String time) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailablePlacesOfUser");
      query.setLong("userId", userId);
      query.setString("time", time);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<Place> getAvailablePlacesOfOrg(Long orgId, String time) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailablePlacesOfOrg");
      query.setLong("orgId", orgId);
      query.setString("time", time);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<String> getAvailableTimesOfOrg(Long orgId) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailableTimesOfOrg");
      query.setLong("orgId", orgId);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<String> getAvailableTimesOfUser(Long userId) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailableTimesOfUser");
      query.setLong("userId", userId);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<Shapefile> getShapefiles(Long placeId, String time) {
      Query query = getSession().getNamedQuery("Shapefile.getShapefilesByPlaceAndTime");
      query.setLong("placeId", placeId);
      query.setString("time", time);
      return query.list();
   }

   @Override
   public Shapefile getShapefile(Long placeId, String time, String category) {
      Query query = getSession().getNamedQuery("Shapefile.getShapefileByPTC");
      query.setLong("placeId", placeId);
      query.setString("time", time);
      query.setString("category", category);
      return (Shapefile) query.uniqueResult();
   }

   @Override
   public List<Shapefile> getAvailableFilesOfUser(Long userId) {
      String hql = "select file from com.rockontrol.yaogan.model.Shapefile as file,"
            + "com.rockontrol.yaogan.model.Place p,com.rockontrol.yaogan.model.UserPlace up "
            + "  where file.placeId=up.placeId and p.id=file.placeId and up.userId=:userId";
      Query query = getSession().createQuery(hql);
      query.setLong("userId", userId);
      List<Shapefile> list = query.list();
      return list;
   }

   @Override
   public List<Shapefile> getShapefilesOfOrg(Long orgId) {
      String hql = "select file from com.rockontrol.yaogan.model.Shapefile file "
            + ",com.rockontrol.yaogan.model.Place p where p.orgId=:orgId and file.placeId=p.id";
      Query query = getSession().createQuery(hql);
      query.setLong("orgId", orgId);
      return query.list();
   }

   @Override
   public long getShapefileCountOfOrg(Long orgId) {
      String hql = "select count(file) from com.rockontrol.yaogan.model.Shapefile file "
            + ",com.rockontrol.yaogan.model.Place p where p.orgId=:orgId and file.placeId=p.id";
      Query query = getSession().createQuery(hql);
      query.setLong("orgId", orgId);
      Long count = (Long) query.uniqueResult();
      return count;
   }

   @Override
   public List<Shapefile> getShapefilesOfOrg(Long orgId, Long placeId, String time) {
      String hql = "select file from com.rockontrol.yaogan.model.Shapefile file "
            + ",com.rockontrol.yaogan.model.Place p where file.placeId=:placeId and file.placeId=p.id and p.orgId=:orgId "
            + " and file.shootTime=:time order by file.placeId,file.shootTime desc";
      Query query = getSession().createQuery(hql);
      query.setLong("orgId", orgId);
      query.setString("time", time);
      query.setLong("placeId", placeId);
      return query.list();
   }

   private static final String FILTER_HQL_TEMPLATE = "select sf from com.rockontrol.yaogan.model.Shapefile"
         + " where 1 = 1";

   @Override
   public List<Shapefile> filter(Long placeId, String time, int startIndex, int maxCount) {
      String hql = _genFilterHql(placeId, time);
      Object[] params = _genParams(placeId, time);
      return findByPage(hql, startIndex, maxCount, params);
   }

   @Override
   public long getCount(Long placeId, String time) {
      String hql = _genFilterHql(placeId, time);
      Object[] params = _genParams(placeId, time);
      return getCount(hql, params);
   }

   private String _genFilterHql(Long placeId, String time) {
      StringBuilder sb = new StringBuilder();
      sb.append(FILTER_HQL_TEMPLATE);
      if (placeId != null) {
         sb.append(" and placeId = :placeId");
      }
      if (time != null) {
         sb.append(" and time = :time");
      }
      return sb.toString();
   }

   private Object[] _genParams(Long placeId, String time) {
      if (placeId != null && time != null) {
         return new Object[] { placeId, time };
      }
      if (placeId != null) {
         return new Object[] { placeId };
      }
      if (time != null) {
         return new Object[] { time };
      }
      return null;
   }

   @Override
   public long getShapefileCountOfUser(User caller) {
      String hql = "select count(file) from com.rockontrol.yaogan.model.Shapefile as file,"
            + "com.rockontrol.yaogan.model.Place p,com.rockontrol.yaogan.model.UserPlace up "
            + "  where file.placeId=up.placeId and p.id=file.placeId and up.userId=:userId";
      Query query = getSession().createQuery(hql);
      query.setLong("userId", caller.getId());
      long count = (Long) query.uniqueResult();
      return count;
   }

   @Override
   public List<Shapefile> getShapefilesOfOrgByPage(Long orgId, int startIndex,
         int maxCount) {
      String hql = "select file from com.rockontrol.yaogan.model.Shapefile file "
            + ",com.rockontrol.yaogan.model.Place p where p.orgId=:orgId and file.placeId=p.id";
      Query query = getSession().createQuery(hql);
      query.setLong("orgId", orgId);
      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

   @Override
   public List<Shapefile> getShapefilesOfUserByPage(Long userId, int startIndex,
         int maxCount) {
      String hql = "select file from com.rockontrol.yaogan.model.Shapefile as file,"
            + "com.rockontrol.yaogan.model.Place p,com.rockontrol.yaogan.model.UserPlace up "
            + "  where file.placeId=up.placeId and p.id=file.placeId and up.userId=:userId";
      Query query = getSession().createQuery(hql);
      query.setLong("userId", userId);
      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

   @Override
   public List<Shapefile> filterShapefilesOfOrgByPage(Long orgId, Long placeId,
         String time, int startIndex, int maxCount) {
      String hql = "select file from com.rockontrol.yaogan.model.Shapefile file "
            + ",com.rockontrol.yaogan.model.Place p where file.placeId=:placeId and file.placeId=p.id and p.orgId=:orgId "
            + " and file.shootTime=:time order by file.placeId,file.shootTime desc";
      Query query = getSession().createQuery(hql);
      query.setLong("orgId", orgId);
      query.setString("time", time);
      query.setLong("placeId", placeId);
      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

   @Override
   public List<Shapefile> filterShapefilesOfUserByPage(Long userId, Long placeId,
         String time, int startIndex, int maxCount) {
      String hql = "select file from com.rockontrol.yaogan.model.Shapefile as file,"
            + "com.rockontrol.yaogan.model.Place p,com.rockontrol.yaogan.model.UserPlace up "
            + "  where file.placeId=:placeId and file.placeId=up.placeId and p.id=file.placeId and up.userId=:userId and file.shootTime=:time "
            + "order by file.placeId,file.shootTime desc";
      Query query = getSession().createQuery(hql);
      query.setLong("userId", userId);
      query.setString("time", time);
      query.setLong("placeId", placeId);
      query.setFirstResult(startIndex);
      query.setMaxResults(maxCount);
      return query.list();
   }

   @Override
   public long filterCountOfOrg(Long orgId, Long placeId, String time) {
      String hql = "select count(file) from com.rockontrol.yaogan.model.Shapefile file "
            + ",com.rockontrol.yaogan.model.Place p where file.placeId=:placeId and file.placeId=p.id and p.orgId=:orgId "
            + " and file.shootTime=:time order by file.placeId,file.shootTime desc";
      Query query = getSession().createQuery(hql);
      query.setLong("orgId", orgId);
      query.setString("time", time);
      query.setLong("placeId", placeId);
      long count = (Long) query.uniqueResult();
      return count;
   }

   @Override
   public long filterCountOfUser(Long userId, Long placeId, String time) {
      String hql = "select count(file) from com.rockontrol.yaogan.model.Shapefile as file,"
            + "com.rockontrol.yaogan.model.Place p,com.rockontrol.yaogan.model.UserPlace up "
            + "  where file.placeId=:placeId and file.placeId=up.placeId and p.id=file.placeId and up.userId=:userId and file.shootTime=:time "
            + "order by file.placeId,file.shootTime desc";
      Query query = getSession().createQuery(hql);
      query.setLong("userId", userId);
      query.setString("time", time);
      query.setLong("placeId", placeId);
      long count = (Long) query.uniqueResult();
      return count;
   }

}
