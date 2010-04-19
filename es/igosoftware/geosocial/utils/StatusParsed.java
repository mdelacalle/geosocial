package es.igosoftware.geosocial.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StatusParsed {

   private String status   = "";
   ArrayList<URL> urls     = new ArrayList<URL>();
   ArrayList<URL> photos   = new ArrayList<URL>();
   List<String>   mentions = new ArrayList<String>();


   public List<String> getMentions() {
      return mentions;
   }


   public void setMentions(final List<String> _mentions) {
      this.mentions = _mentions;
   }


   public String getStatus() {
      return status;
   }


   public void setStatus(final String _status) {
      this.status = _status;
   }


   public ArrayList<URL> getUrls() {
      return urls;
   }


   public void setUrls(final ArrayList<URL> _urls) {
      this.urls = _urls;
   }


   public ArrayList<URL> getPhotos() {
      return photos;
   }


   public void setPhotos(final ArrayList<URL> _photos) {
      this.photos = _photos;
   }


   @Override
   public String toString() {

      String toString = "Mentions:";

      String mentionS = "";
      for (final String mention : mentions) {
         mentionS = mentionS + " " + mention;
      }

      toString = toString + mentionS;

      String urlsS = " URLs:";

      for (final URL url : urls) {
         urlsS = urlsS + " " + url.toString();
      }
      toString = toString + urlsS;

      String photosS = " Photos:";

      for (final URL photo : photos) {
         photosS = photosS + " " + photo.toString();
      }
      toString = toString + photosS;


      return toString;
   }

}
