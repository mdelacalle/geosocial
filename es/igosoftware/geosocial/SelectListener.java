package es.igosoftware.geosocial;

import es.igosoftware.geosocial.gui.Styles;
import es.igosoftware.geosocial.gui.TwitterMarker;
import es.igosoftware.geosocial.utils.Logger;
import es.igosoftware.geosocial.utils.TwitterUtils;
import es.igosoftware.geosocial.utils.URLParser;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.net.MalformedURLException;

import winterwell.jtwitter.Twitter.Status;

public class SelectListener
         implements
            gov.nasa.worldwind.event.SelectListener {

   private final WorldWindowGLCanvas wwd;


   public SelectListener(final WorldWindowGLCanvas _wwd) {
      this.wwd = _wwd;
   }


   @Override
   public void selected(final SelectEvent event) {
      if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
         doLeftClick(event);
      }
   }


   private void doLeftClick(final SelectEvent event) {

      if (wwd.getModel().getLayers().getLayerByName("ANNOTATIONS") != null) {
         wwd.getModel().getLayers().remove(wwd.getModel().getLayers().getLayerByName("ANNOTATIONS"));
      }


      final Object eventObject = event.getTopObject();


      if (eventObject instanceof TwitterMarker) {


         final Status status = ((TwitterMarker) eventObject).getStatus();
         final Position position = ((TwitterMarker) eventObject).getPosition();

         final AnnotationAttributes aa = new AnnotationAttributes();
         aa.setBackgroundColor(Styles.blueTwitter);
         aa.setBorderColor(Color.BLACK);
         aa.setSize(new Dimension(400, 0));
         aa.setHighlightScale(1);
         aa.setInsets(new Insets(12, 12, 12, 20));
         aa.setFont(Font.decode("SansSerif-PLAIN-14"));
         aa.setVisible(false);
         aa.setTextColor(Color.BLACK);

         GlobeAnnotation ga;
         try {

            Logger.DEBUG("<p>\n<b><font color=\"#664400\">" + status.getUser().getName() + "</font></b>" + "<img src=\""
                         + status.getUser().getProfileImageUrl().toURL() + "\">\n</p><p>"
                         + TwitterUtils.decorateMsg(URLParser.parseStatus(status), status) + "</p>");

            ga = new GlobeAnnotation("<p>\n<b><font color=\"#664400\">" + status.getUser().getName() + "</font></b>"
                                     + "<img src=\"" + status.getUser().getProfileImageUrl().toURL() + "\">\n</p><p>"
                                     + TwitterUtils.decorateMsg(URLParser.parseStatus(status), status) + "</p>", position, aa);


            final AnnotationLayer annotationsLayer = new AnnotationLayer();
            annotationsLayer.addAnnotation(ga);
            annotationsLayer.setName("ANNOTATIONS");
            wwd.getModel().getLayers().add(annotationsLayer);
         }
         catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

      }

   }
}
