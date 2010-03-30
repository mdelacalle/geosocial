package es.igosoftware.geosocial;


public class TwittTest {

   /**
    * @param args
    */
   public static void main(final String[] args) {


      final AuthDialog auth = new AuthDialog();
      auth.pack();
      auth.setVisible(true);


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
