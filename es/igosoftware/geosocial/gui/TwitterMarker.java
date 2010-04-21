package es.igosoftware.geosocial.gui;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;
import winterwell.jtwitter.Twitter.Status;

public class TwitterMarker
         extends
            BasicMarker {

   private final Status status;


   public TwitterMarker(final Position position,
                        final MarkerAttributes attrs,
                        final Status _status) {

      super(position, attrs);
      status = _status;
   }


   public Status getStatus() {
      return status;
   }

}
