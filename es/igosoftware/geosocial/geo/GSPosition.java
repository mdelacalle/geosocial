package es.igosoftware.geosocial.geo;

import winterwell.jtwitter.Twitter.Status;

public class GSPosition {

   private final String  latitude;
   private final String  longitude;
   private final boolean geocoded;
   private final Status  status;


   public GSPosition(String position,
                     final Status _status,
                     final boolean _geocoded) {

      this.geocoded = _geocoded;

      if (position == null) {
         position = "0,0";
      }
      this.status = _status;
      this.latitude = position.substring(0, position.indexOf(","));
      this.longitude = position.substring(position.indexOf(",") + 1);

   }


   public boolean isGeocoded() {
      return geocoded;
   }


   public String getLatitude() {
      return latitude;
   }


   public String getLongitude() {
      return longitude;
   }


   public Status getStatus() {
      return status;
   }


}
