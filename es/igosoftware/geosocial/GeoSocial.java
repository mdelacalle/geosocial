package es.igosoftware.geosocial;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.util.StatusBar;

import java.awt.BorderLayout;

import javax.swing.JApplet;

import winterwell.jtwitter.Twitter;

/**
 * @author manueldelacallealonso
 * @date mon 03 29 2010 Main class that launch a geoApplet
 */

public class GeoSocial
         extends
            JApplet {

   /**
    * 
    */
   private static final long   serialVersionUID = 1555517063937102570L;
   private WorldWindowGLCanvas wwd;
   private StatusBar           statusBar;
   private Twitter             tw;


   public GeoSocial() {}


   @Override
   public void init() {
      try {

         //   authenticate();

         // Create World Window GL Canvas
         this.wwd = new WorldWindowGLCanvas();
         this.getContentPane().add(this.wwd, BorderLayout.CENTER);

         // Create the default model as described in the current worldwind properties.
         final Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
         this.wwd.setModel(m);

         // Add the status bar
         this.statusBar = new StatusBar();
         this.getContentPane().add(this.statusBar, BorderLayout.PAGE_END);

         // Forward events to the status bar to provide the cursor position info.
         this.statusBar.setEventSource(this.wwd);

      }
      catch (final Throwable e) {
         e.printStackTrace();
      }
   }


   private void authenticate() {


      final AuthDialog auth = new AuthDialog();

      auth.setAlwaysOnTop(true);
      auth.setLocation(this.getWidth() / 2, this.getHeight() / 2);

      auth.pack();
      auth.setVisible(true);


   }


   @Override
   public void stop() {
      // Shut down World Wind
      WorldWind.shutDown();
   }

}
