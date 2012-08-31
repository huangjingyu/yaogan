package com.rockontrol.yaogan.controller.gis;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rockontrol.yaogan.service.EcoFactorComputeService;

@Controller
@RequestMapping(value = "/yaogan/gis/rest")
public class EcoFactorComputeController {
   @Autowired
   private EcoFactorComputeService _computeService;

   @RequestMapping(value = { "/ajax/abio" }, method = RequestMethod.POST)
   public ResponseEntity<String> computeAbio(@RequestBody EcoComputeVO vo,
         HttpServletResponse httpResponse) throws IOException {
      double result = 0;
      try {
         if (vo.getBbox() == null && vo.getBbox() == null) {
            result = _computeService.computeAbio(vo.getRegion(), vo.getYear());
         } else {
            if (vo.getBbox() != null) {
               double[] box = new double[4];
               String s[] = vo.getBbox().split(",");
               for (int i = 0; i < 4; i++) {
                  box[i] = Double.parseDouble(s[i]);
               }
               result = _computeService.computeAbio(vo.getRegion(), vo.getYear(),
                     box[0], box[1], box[2], box[3]);
            } else {
               result = _computeService.computeAbio(vo.getRegion(), vo.getYear(),
                     vo.getBoundary());
            }
         }
         HttpHeaders responseHeaders = new HttpHeaders();
         responseHeaders.add("Content-Type", "text/plain;charset=UTF-8");
         return new ResponseEntity<String>(result + "", responseHeaders, HttpStatus.OK);
      } catch (IOException e) {
         e.printStackTrace();
         httpResponse.setStatus(500);
      }
      return null;
   }

   @RequestMapping(value = { "/ajax/aveg" }, method = RequestMethod.POST)
   public ResponseEntity<String> computeAveg(@RequestBody EcoComputeVO vo,
         HttpServletResponse httpResponse) {
      double result = 0;
      try {
         if (vo.getBbox() == null && vo.getBbox() == null) {
            result = _computeService.computeAveg(vo.getRegion(), vo.getYear());
         } else {
            if (vo.getBbox() != null) {
               double[] box = new double[4];
               String s[] = vo.getBbox().split(",");
               for (int i = 0; i < 4; i++) {
                  box[i] = Double.parseDouble(s[i]);
               }
               result = _computeService.computeAveg(vo.getRegion(), vo.getYear(),
                     box[0], box[1], box[2], box[3]);
            } else {
               result = _computeService.computeAveg(vo.getRegion(), vo.getYear(),
                     vo.getBoundary());
            }

         }
         HttpHeaders responseHeaders = new HttpHeaders();
         responseHeaders.add("Content-Type", "text/plain;charset=UTF-8");
         return new ResponseEntity<String>(result + "", responseHeaders, HttpStatus.OK);
      } catch (IOException e) {
         e.printStackTrace();
         httpResponse.setStatus(500);
      }
      return null;
   }

   @RequestMapping(value = { "/ajax/aero" }, method = RequestMethod.POST)
   public ResponseEntity<String> computeAero(@RequestBody EcoComputeVO vo,
         HttpServletResponse httpResponse) {
      double result = 0;
      try {
         if (vo.getBbox() == null && vo.getBbox() == null) {
            result = _computeService.computeAero(vo.getRegion(), vo.getYear());
         } else {
            if (vo.getBbox() != null) {
               double[] box = new double[4];
               String s[] = vo.getBbox().split(",");
               for (int i = 0; i < 4; i++) {
                  box[i] = Double.parseDouble(s[i]);
               }
               result = _computeService.computeAero(vo.getRegion(), vo.getYear(),
                     box[0], box[1], box[2], box[3]);
            } else {
               result = _computeService.computeAero(vo.getRegion(), vo.getYear(),
                     vo.getBoundary());
            }
         }
         HttpHeaders responseHeaders = new HttpHeaders();
         responseHeaders.add("Content-Type", "text/plain;charset=UTF-8");
         return new ResponseEntity<String>(result + "", responseHeaders, HttpStatus.OK);
      } catch (IOException e) {
         e.printStackTrace();
         httpResponse.setStatus(500);
      }
      return null;
   }

}
