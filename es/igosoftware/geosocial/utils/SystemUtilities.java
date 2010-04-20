package es.igosoftware.geosocial.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class SystemUtilities {


   public static boolean isMac() {

      if (System.getProperty("os.name").equals("Mac OS X")) {
         return true;
      }
      return false;
   }


   public static void openBrowser(final URL url) {
      final Desktop desktop = Desktop.getDesktop();
      try {
         desktop.browse(url.toURI());
      }
      catch (final IOException e) {
         Logger.FATAL(e.toString());
      }
      catch (final URISyntaxException e) {
         Logger.FATAL(e.toString());
      }
   }
}
