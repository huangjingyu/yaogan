package com.rockontrol.yaogan.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class CompressUtil {

   /**
    * uncompress zip file
    * 
    * @param source
    * @param dest
    * @param charset
    * @throws IOException
    */
   public static void unZip(String source, String dest, Charset charset)
         throws IOException {
      ZipFile zipFile = new ZipFile(source, charset.toString());
      String base = dest;
      Enumeration<ZipArchiveEntry> enu = zipFile.getEntries();
      while (enu.hasMoreElements()) {
         ZipArchiveEntry entry = enu.nextElement();
         String name = entry.getName();
         if (name.endsWith("/")) {
            File f = new File(base + File.separator
                  + name.substring(0, name.length() - 1));
            f.mkdirs();
            continue;
         }
         String path = base + File.separator + name;
         InputStream input = zipFile.getInputStream(entry);
         saveTo(input, path);
         input.close();
      }
      zipFile.close();
   }

   public static void unZip(String source, String dest) throws IOException {
      unZip(source, dest, Charset.defaultCharset());
   }

   public static void saveTo(InputStream is, String dest) throws IOException {
      OutputStream os = new FileOutputStream(dest);
      byte b[] = new byte[1024];
      int l = 0;
      while ((l = is.read(b)) > 0) {
         os.write(b, 0, l);
      }
      os.close();
   }

}
