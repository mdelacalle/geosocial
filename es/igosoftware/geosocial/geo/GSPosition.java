package es.igosoftware.geosocial.geo;

public class GSPosition {

   private final String  latitude;
   private final String  longitude;
   private final boolean geocoded;


   public GSPosition(String position,
                     final boolean _geocoded) {

      this.geocoded = _geocoded;

      if (position == null) {
         position = "0,0";
      }

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


}
