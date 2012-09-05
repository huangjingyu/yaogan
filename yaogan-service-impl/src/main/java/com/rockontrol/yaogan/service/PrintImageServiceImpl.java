package com.rockontrol.yaogan.service;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaogan.gis.mgr.IDataStoreManager;
import org.yaogan.gis.mgr.SimpleDataStoreManagerImpl;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.User;
import com.sun.media.jai.codec.PNGEncodeParam;

@Service
public class PrintImageServiceImpl implements IPrintImageService {

   @Autowired
   private IYaoganService yaoganService;

   private IDataStoreManager dataStoreManager = new SimpleDataStoreManagerImpl();

   @Override
   public void addShapeLayer(User caller, Long placeId, String category, String time)
         throws Exception {

      Shapefile shapefile = yaoganService.getShapefile(caller, placeId, category, time);
      FileDataStore store = dataStoreManager.getDataStore(shapefile.getFilePath());
      FeatureSource featureSource = store.getFeatureSource();

      File sldFile = new File("d:\\shape.sld");
      StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
      SLDParser sldParser = new SLDParser(styleFactory, sldFile);
      Style[] styles = sldParser.readXML();

      FeatureLayer layer = new FeatureLayer(featureSource, styles[0]);
      MapContent mapContent = new MapContent();
      mapContent.addLayer(layer);
      // 设置shapefile区域 //TODO
      ReferencedEnvelope mapArea = new ReferencedEnvelope();
      // 初始化渲染器
      StreamingRenderer sr = new StreamingRenderer();
      sr.setMapContent(mapContent);
      // 初始化输出图像
      BufferedImage bi = new BufferedImage(795, 1124, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = bi.createGraphics();
      // 使用抗锯齿模式完成呈现
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
      // 使用某种抗锯齿形式完成文本呈现
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      Rectangle rect = new Rectangle(0, 0, 795, 1124);
      // 绘制地图
      sr.paint(g, rect, mapArea);
      // 编码图像
      PNGEncodeParam encodeParam = PNGEncodeParam.getDefaultEncodeParam(bi);
      if (encodeParam instanceof PNGEncodeParam.Palette) {
         PNGEncodeParam.Palette p = (PNGEncodeParam.Palette) encodeParam;
         byte[] b = new byte[] { -127 };
         p.setPaletteTransparency(b);
      }
      // // 将图像数据输出到Servlet相应中
      // response.setContentType("image/png");
      // ServletOutputStream out = response.getOutputStream();
      // com.sun.media.jai.codec.ImageEncoder encoder =
      // ImageCodec.createImageEncoder(
      // "PNG", out, encodeParam);
      // encoder.encode(bi.getData(), bi.getColorModel());
      FileOutputStream out = new FileOutputStream("c:\\Users\\RK\\Desktop\\test1.jpg");
      ImageIO.write(bi, "jpeg", out);
      out.close();
   }
}
