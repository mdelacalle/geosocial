package es.igosoftware.geosocial;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.util.StatusBar;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
   private final AuthDialog    auth             = new AuthDialog();
   private JPanel              twitterPanel     = new JPanel();


   public GeoSocial() {}


   @Override
   public void init() {
      try {

         authenticate();


         // Create World Window GL Canvas
         this.wwd = new WorldWindowGLCanvas();


         // Create the default model as described in the current worldwind properties.
         final Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
         this.wwd.setModel(m);

         // Add the status bar
         this.statusBar = new StatusBar();
         this.getContentPane().add(this.statusBar, BorderLayout.PAGE_END);

         // Forward events to the status bar to provide the cursor position info.
         this.statusBar.setEventSource(this.wwd);


         final JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

         sp.add(getTwitterPanel());
         sp.add(this.wwd);

         sp.setDividerLocation(this.getWidth() / 5);
         this.getContentPane().add(sp);


      }
      catch (final Throwable e) {
         e.printStackTrace();
      }
   }


   private Component getTwitterPanel() {

      twitterPanel.add(new JLabel("not connected on twitter"));
      return twitterPanel;
   }


   private void authenticate() {

      final Thread thread = new Thread(new Runnable() {
         public void run() {
            auth.setAlwaysOnTop(true);
            auth.setLocation((GeoSocial.this.getWidth() / 2) - 100, GeoSocial.this.getHeight() / 2);

            auth.pack();
            auth.setVisible(true);
            initializeTwitter();
         }


         private void initializeTwitter() {

            while (tw == null) {
               try {
                  tw = auth.getTwitterObject();
                  tw.getScreenName();
               }
               catch (final NullPointerException e) {}
            }

            twitterPanel = new JPanel();
            twitterPanel.add(new JLabel(tw.getScreenName()));
            twitterPanel.repaint();

         }
      });
      thread.setDaemon(true);
      thread.start();


   }


   @Override
   public void stop() {
      // Shut down World Wind
      WorldWind.shutDown();
   }

}
