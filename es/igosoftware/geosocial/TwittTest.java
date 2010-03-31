package es.igosoftware.geosocial;

import java.awt.Dimension;

import javax.swing.JFrame;

import winterwell.jtwitter.Twitter;
import es.igosoftware.geosocial.gui.TwitterPanel;


public class TwittTest {


   final static JFrame       _f  = new JFrame("twp");

   final static TwitterPanel _tp = new TwitterPanel();

   static Twitter            _tw = new Twitter();


   /**
    * @param args
    */

   @SuppressWarnings("all")
   public static void main(final String[] args) {


      TwittTest._f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      TwittTest._tw = new Twitter("geoigotwit", "hola12");


      //  testUserPanel();

      testRefreshPanel();


      //      tp.refreshTwits(tw);
      //
      //      //      tp.setUser(tw.getUser(tw.getScreenName()));
      //      f.add(tp);
      //      f.pack();
      //      f.setSize(new Dimension(200, 600));
      //      f.setVisible(true);


      //Scheduled Services
      //      final Timer timer = new Timer();
      //      timer.scheduleAtFixedRate(new TwitTask(tw), 0, 5000);


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


   private static void testRefreshPanel() {
      testUserPanel();
      TwittTest._tp.refreshTwits(TwittTest._tw);
      TwittTest._f.add(TwittTest._tp);
      TwittTest._f.pack();
      TwittTest._f.setSize(new Dimension(200, 600));
      TwittTest._f.setVisible(true);
   }


   private static void testUserPanel() {
      TwittTest._tp.setUser(TwittTest._tw.getUser(TwittTest._tw.getScreenName()));
      TwittTest._f.add(TwittTest._tp);
      TwittTest._f.pack();
      TwittTest._f.setSize(new Dimension(200, 600));
      TwittTest._f.setVisible(true);
   }
}
