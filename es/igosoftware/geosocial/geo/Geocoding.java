package es.igosoftware.geosocial.geo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Geocoding {

   static String _coordinates = "0,0";


   public static String getCoordinates(final String name) {

      final String path = "http://ws.geonames.org/searchJSON?formatted=true&q=" + name + "&maxRows=1&lang=es&style=full";

      URL url;
      try {
         url = new URL(path);


         final URLConnection connection = url.openConnection();

         connection.connect();

         final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String sCadena = "";
         String response = "";
         while ((sCadena = in.readLine()) != null) {
            response = response + " " + sCadena;
         }


         final JSONObject responseJson = new JSONObject(response);

         final JSONArray array = (JSONArray) responseJson.get("geonames");

         int i = 0;
         for (i = 0; i < array.length(); i++) {
            Geocoding._coordinates = array.getJSONObject(i).get("lat") + "," + array.getJSONObject(i).get("lng");
         }

      }
      catch (final MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final JSONException e) {

         e.printStackTrace();
      }

      return Geocoding._coordinates;
   }
}
