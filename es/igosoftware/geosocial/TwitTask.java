package es.igosoftware.geosocial;

import java.util.List;
import java.util.TimerTask;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;

public class TwitTask
         extends
            TimerTask {

   final Twitter tw;


   public TwitTask(final Twitter _tw) {
      tw = _tw;
   }


   @Override
   public void run() {

      final List<Status> statuses = tw.getPublicTimeline();

      for (final Status status : statuses) {
         System.out.println(status.getUser().getName() + ", location:" + status.getUser().getLocation() + ", User:" + status.text);
      }

      System.out.println("cada 5 segs.");

   }

}
