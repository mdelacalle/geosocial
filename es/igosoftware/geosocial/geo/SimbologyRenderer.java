package es.igosoftware.geosocial.geo;

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

import java.awt.Color;
import java.util.ArrayList;

public class SimbologyRenderer {

   public static void renderPositions(final ArrayList<String> positions,
                                      final WorldWindowGLCanvas wwd) {

      final BasicMarkerAttributes markerAttrs = new BasicMarkerAttributes(new Material(Color.BLUE),
               BasicMarkerShape.ORIENTED_CONE, 1d, 6, 3);
      final ArrayList<Marker> markersList = new ArrayList<Marker>();
      for (final String position : positions) {

         final String latitude = position.substring(0, position.indexOf(","));
         final String longitude = position.substring(position.indexOf(",") + 1);

         final BasicMarker marker = new BasicMarker(new Position(new LatLon(Angle.fromDegrees(Double.parseDouble(latitude)),
                  Angle.fromDegrees(Double.parseDouble(longitude))), 0), markerAttrs);

         markersList.add(marker);
      }

      final MarkerLayer twitterLayer = new MarkerLayer();
      twitterLayer.setName("twitter layer");
      twitterLayer.setOverrideMarkerElevation(true);
      twitterLayer.setKeepSeparated(false);
      twitterLayer.setElevation(10d);
      twitterLayer.setMarkers(markersList);
      wwd.getModel().getLayers().add(twitterLayer);


   }

}
