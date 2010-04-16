package es.igosoftware.geosocial.utils;

import java.net.URL;
import java.util.ArrayList;

public class StatusParsed {

   private String    status   = "";
   ArrayList<URL>    urls     = new ArrayList<URL>();
   ArrayList<URL>    photos   = new ArrayList<URL>();
   ArrayList<String> mentions = new ArrayList<String>();


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
}
