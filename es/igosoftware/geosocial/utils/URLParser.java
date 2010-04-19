package es.igosoftware.geosocial.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winterwell.jtwitter.Twitter.Status;

public class URLParser {


   static StatusParsed   statusParsed = new StatusParsed();
   static ArrayList<URL> urls         = new ArrayList<URL>();
   static ArrayList<URL> photos       = new ArrayList<URL>();


   public static StatusParsed parseStatus(final Status status) {
      URLParser.statusParsed = new StatusParsed();
      URLParser.statusParsed.setMentions(status.getMentions());
      URLParser.urls = new ArrayList<URL>();
      URLParser.photos = new ArrayList<URL>();

      parseURLS(status);

      return URLParser.statusParsed;
   }


   private static void parseURLS(final Status status) {

      final Pattern pattern = Pattern.compile("(http\\:\\/\\/[^ ]*)");
      final Matcher matcher = pattern.matcher(status.toString());

      while (matcher.find()) {
         try {

            if (isPhoto(matcher.group())) {
               URLParser.photos.add(new URL(matcher.group()));
            }
            else {
               URLParser.urls.add(new URL(matcher.group()));
            }
         }
         catch (final MalformedURLException e) {
            e.printStackTrace();
         }
      }

      URLParser.statusParsed.setUrls(URLParser.urls);
      URLParser.statusParsed.setPhotos(URLParser.photos);

   }


   private static boolean isPhoto(final String group) {

      if (group.contains("moby.to") || group.contains("twitpic.com") || group.contains("ow.ly") || group.contains("yfrog")) {
         return true;
      }
      return false;

   }

}
