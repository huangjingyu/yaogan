package com.rockontrol.yaogan.controller.rest;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;
import com.rockontrol.yaogan.util.Constants;
import com.rockontrol.yaogan.util.Pair;
import com.rockontrol.yaogan.vo.EnvStats;

@Controller
@RequestMapping(value = "/api/envstats")
public class EnvStatsController {

   @Autowired
   private ISecurityManager _secMng;

   @Autowired
   private IYaoganService _service;

   @SuppressWarnings("unchecked")
   @RequestMapping(value = "place/{placeId}/times/{times}")
   public Pair<String, EnvStats>[] envStatsOfDiffTimes(
         @PathVariable("placeId") Long placeId, @PathVariable("times") String timesStr) {
      User user = _secMng.currentUser();
      String[] times = Constants.PAT_COMMA.split(timesStr);
      Arrays.sort(times);
      EnvStats[] statsArr = _service.getEnvStats(user, placeId, times);
      Pair<String, EnvStats>[] pairs = new Pair[times.length];
      for (int i = 0; i < times.length; i++) {
         pairs[i] = new Pair<String, EnvStats>(times[i], statsArr[i]);
      }
      return pairs;
   }

   @SuppressWarnings("unchecked")
   @RequestMapping(value = "time/{time}/places/{placeIds}")
   public Pair<String, EnvStats>[] envStatsOfDiffPlaces(
         @PathVariable("time") String time, @PathVariable("placeIds") String placeIdsStr) {
      User user = _secMng.currentUser();
      String[] arr = Constants.PAT_COMMA.split(placeIdsStr);
      Long[] placeIds = new Long[arr.length];
      for (int i = 0; i < arr.length; i++) {
         placeIds[i] = Long.valueOf(arr[i]);
      }
      EnvStats[] statsArr = _service.getEnvStats(user, placeIds, time);
      Pair<String, EnvStats>[] pairs = new Pair[placeIds.length];
      for (int i = 0; i < placeIds.length; i++) {
         Place place = _service.getPlaceById(placeIds[i]);
         pairs[i] = new Pair<String, EnvStats>(place == null ? "" : place.getName(),
               statsArr[i]);
      }
      return pairs;
   }
}
