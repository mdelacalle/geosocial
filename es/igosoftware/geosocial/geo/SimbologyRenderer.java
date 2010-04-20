package es.igosoftware.geosocial.geo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import es.igosoftware.geosocial.utils.Logger;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

public class SimbologyRenderer {


   static ArrayList<GSPosition> positions = new ArrayList<GSPosition>();


   public static void renderPositions(final ArrayList<GSPosition> _positions,
                                      final WorldWindowGLCanvas wwd) {

      SimbologyRenderer.positions = _positions;

      final BasicMarkerAttributes markerGPSAttrs = new BasicMarkerAttributes(new Material(Color.GREEN), BasicMarkerShape.SPHERE,
               1d, 6, 3);
      final BasicMarkerAttributes markerGeocodedAttrs = new BasicMarkerAttributes(new Material(Color.YELLOW),
               BasicMarkerShape.SPHERE, 1d, 6, 3);
      final ArrayList<Marker> markersGPSList = new ArrayList<Marker>();
      final ArrayList<Marker> markersGeocodedList = new ArrayList<Marker>();
      for (GSPosition position : SimbologyRenderer.positions) {


         //When geocoding, is normal to have several positions with the same coordinates, now
         // we verify positions and we change the coordinate to correct visualization. We paint on different colors
         // the gps coordinates and the geocoding coodinates


         Logger.DEBUG("latitude:" + position.getLatitude() + "," + "longitude:" + position.getLongitude());

         //the gps coordinates always are very long
         if (!position.isGeocoded()) {
            final BasicMarker marker = new BasicMarker(new Position(new LatLon(
                     Angle.fromDegrees(Double.parseDouble(position.getLatitude())),
                     Angle.fromDegrees(Double.parseDouble(position.getLongitude()))), 0), markerGPSAttrs);
            markersGPSList.add(marker);
         }

         else {

            position = randomPosition(position);
            final BasicMarker geoCodedmarker = new BasicMarker(new Position(new LatLon(
                     Angle.fromDegrees(Double.parseDouble(position.getLatitude())),
                     Angle.fromDegrees(Double.parseDouble(position.getLongitude()))), 0), markerGeocodedAttrs);
            markersGeocodedList.add(geoCodedmarker);

         }
      }

      final MarkerLayer twitterGPSLayer = new MarkerLayer();

      twitterGPSLayer.setName("twitter GPS layer");
      twitterGPSLayer.setOverrideMarkerElevation(true);
      twitterGPSLayer.setKeepSeparated(false);
      twitterGPSLayer.setElevation(10d);
      twitterGPSLayer.setMarkers(markersGPSList);
      wwd.getModel().getLayers().add(twitterGPSLayer);

      final MarkerLayer twitterGeocodedLayer = new MarkerLayer();

      //      final AnnotationLayer twitterGeocodedLayer = new AnnotationLayer();

      twitterGeocodedLayer.setName("twitter Geocoded layer");
      twitterGeocodedLayer.setOverrideMarkerElevation(true);
      twitterGeocodedLayer.setKeepSeparated(false);
      twitterGeocodedLayer.setElevation(10d);
      twitterGeocodedLayer.setMarkers(markersGeocodedList);
      wwd.getModel().getLayers().add(twitterGeocodedLayer);


   }


   private static GSPosition randomPosition(final GSPosition geoCodedposition) {
      GSPosition position = geoCodedposition;

      for (final GSPosition positionI : SimbologyRenderer.positions) {

         final String longitude = geoCodedposition.getLongitude();
         final String latitude = geoCodedposition.getLatitude();
         if (positionI.getLongitude().equals(longitude) && positionI.getLatitude().equals(latitude)) {
            final Random rnd = new Random();
            position = new GSPosition(String.valueOf(Double.parseDouble(latitude) + (rnd.nextFloat() / 100)) + ","
                                      + String.valueOf(Double.parseDouble(longitude) + (rnd.nextFloat() / 100)), false);
            break;
         }


      }
      return position;
   }
}
