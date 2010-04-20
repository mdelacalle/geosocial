package es.igosoftware.geosocial.gui;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

public class TwitterMarker
         extends
            BasicMarker {

   private final String m_Data;


   public TwitterMarker(final Position position,
                      final MarkerAttributes attrs,
                      final String data) {

      super(position, attrs);
      m_Data = data;
   }


   public String getText() {
      return m_Data;
   }

}
