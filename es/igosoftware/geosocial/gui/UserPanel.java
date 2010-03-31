package es.igosoftware.geosocial.gui;

import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import winterwell.jtwitter.Twitter;

public class UserPanel
         extends
            JPanel {

   Twitter                   _tw;
   /**
    * 
    */
   private static final long serialVersionUID = -5487447295916513652L;


   public UserPanel(final Twitter tw) {

      this.add(new JLabel("ueje"));
      _tw = tw;
      paintPanel();
   }


   public void paintPanel() {

      this.add(new JLabel("ueje"));

      try {
         final JLabel img = new JLabel(new ImageIcon(_tw.getUser(_tw.getScreenName()).getProfileImageUrl().toURL()));
         this.add(img);
      }
      catch (final MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


}
