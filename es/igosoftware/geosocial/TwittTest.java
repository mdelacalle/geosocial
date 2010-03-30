package es.igosoftware.geosocial;

import java.awt.Dimension;

import javax.swing.JFrame;

import winterwell.jtwitter.Twitter;
import es.igosoftware.geosocial.gui.TwitterPanel;


public class TwittTest {

   /**
    * @param args
    */

   @SuppressWarnings("all")
   public static void main(final String[] args) {


      final JFrame f = new JFrame("twp");

      final TwitterPanel tp = new TwitterPanel();

      final Twitter tw = new Twitter("geoigotwit", "hola12");
      tp.setUser(tw.getUser(tw.getScreenName()));
      f.add(tp);
      f.pack();
      f.setSize(new Dimension(200, 600));
      f.setVisible(true);


      //      final AuthDialog auth = new AuthDialog();
      //      auth.pack();
      //      auth.setVisible(true);


      //
      //      //Authenticate
      //      final Twitter tw = new Twitter("geoigotwit", "hola12");
      //      if (tw.isValidLogin()) {
      //
      //         //  System.out.println(tw.getStatus());
      //         //     System.out.println(tw.getStatus("geoigotwit").getGeo());
      //
      //         //
      //         //         final List<Status> statuses = tw.getHomeTimeline();
      //         //
      //         //         for (final Status status : statuses) {
      //         //            //          System.out.println(status.getUser().getName() + ", location:" + status.getUser().getLocation() + ", User:"
      //         //            //                            + status.text);
      //         //         }
      //         //
      //         //
      //         final List<User> friends = tw.getFriends();
      //
      //
      //         for (final User friend : friends) {
      //            System.out.println(friend.screenName + "--->" + friend.getProfileImageUrl() + "--->" + friend.getStatus());
      //         }
      //      }


   }
}
