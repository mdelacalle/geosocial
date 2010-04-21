package es.igosoftware.geosocial.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import winterwell.jtwitter.Twitter.Status;

public class TwitterUtils {

   public static String decorateMsg(final StatusParsed statusParsed,
                                    final Status status) {

      String htmlStatus = "<html><br><span style=\"font-family:monospace; font-size: 9px ;\">" + status.getText()
                          + "</span></html>";

      final List<String> mentions = statusParsed.getMentions();
      final ArrayList<URL> urls = statusParsed.getUrls();
      final ArrayList<URL> photos = statusParsed.getPhotos();

      for (final String mention : mentions) {
         htmlStatus = htmlStatus.replace("@" + mention, "<font color=red>" + "@" + mention + "</font>");
      }
      for (final URL url : urls) {
         htmlStatus = htmlStatus.replace(url.toString(), "<a href=\"" + url.toString() + "\">" + url.toString() + "</a>");
      }
      //TODO: Visualization of the pics, now is a normal URL
      for (final URL photo : photos) {
         htmlStatus = htmlStatus.replace(photo.toString(), "<a href=\"" + photo.toString() + "\">" + photo.toString() + "</a>");
      }
      return htmlStatus;
   }
}
