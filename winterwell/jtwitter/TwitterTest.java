package winterwell.jtwitter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;

import junit.framework.TestCase;
import winterwell.jtwitter.Twitter.Message;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.Twitter.User;
import winterwell.jtwitter.TwitterException.E403;

/**
 * Unit tests for JTwitter. These only provide partial testing -- sorry.
 * 
 * 
 * @author daniel
 */
@SuppressWarnings("all")
public class TwitterTest
         extends
            TestCase // Comment out to remove the JUnit dependency
{

   private static final String TEST_USER     = "jtwit";
   public static final String  TEST_PASSWORD = "passw0rd";


   public static void main(final String[] args) {
      final TwitterTest tt = new TwitterTest();
      final Method[] meths = TwitterTest.class.getMethods();
      for (final Method m : meths) {
         if (!m.getName().startsWith("test") || (m.getParameterTypes().length != 0)) {
            continue;
         }
         try {
            m.invoke(tt);
            System.out.println(m.getName());
         }
         catch (final IllegalArgumentException e) {
            e.printStackTrace();
         }
         catch (final IllegalAccessException e) {
            e.printStackTrace();
         }
         catch (final InvocationTargetException e) {
            System.out.println("TEST FAILED: " + m.getName());
            System.out.println("\t" + e.getCause());
         }
      }
   }


   public void testBulkShow() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<User> users = tw.bulkShow(Arrays.asList("winterstein", "joehalliwell", "annettemees"));
      assert users.size() == 3 : users;
      assert users.get(1).description != null;
   }


   /**
    * Check that you can send 160 chars if you wants
    */
   public void canSend160() {
      String s = "";
      for (int i = 0; i < 15; i++) {
         s += i + "23456789 ";
      }
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      tw.setStatus(s);
   }


   /**
    * NONDETERMINISTIC! Had to increase sleep time to make it more reliable.
    * 
    * @throws InterruptedException
    */
   public void testDestroyStatus() throws InterruptedException {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final Status s1 = tw.getStatus();
      tw.destroyStatus(s1.getId());
      final Status s0 = tw.getStatus();
      assert s0.id != s1.id : "Status id should differ from that of destroyed status";
   }


   public void testDestroyStatusBad() {
      // Check security failure
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final Status hs = tw.getStatus("winterstein");
      try {
         tw.destroyStatus(hs);
         assert false;
      }
      catch (final Exception ex) {
         // OK
      }
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#follow(java.lang.String)}.
    */
   public void testFollowAndStopFollowing() throws InterruptedException {
      final int lag = 1000; //300000;
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      tw.flush();
      final List<User> friends = tw.getFriends();
      if (!tw.isFollowing("winterstein")) {
         tw.follow("winterstein");
         Thread.sleep(lag);
      }
      assert tw.isFollowing("winterstein") : friends;

      // Stop
      final User h = tw.stopFollowing("winterstein");
      assert h != null;
      Thread.sleep(lag);
      assert !tw.isFollowing("winterstein") : friends;

      // break where no friendship exists
      final User h2 = tw.stopFollowing("winterstein");
      assert h2 == null;

      // Follow
      tw.follow("winterstein");
      Thread.sleep(lag);
      assert tw.isFollowing("winterstein") : friends;

      try {
         final User suspended = tw.follow("Alysha6822");
         assert false : "Trying to follow a suspended user should throw an exception";
      }
      catch (final TwitterException e) {}
   }


   public void testIdenticaAccess() {
      final Twitter jtwit = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      jtwit.setAPIRootUrl("http://identi.ca/api");
      final char salt = (char) ('A' + new Random().nextInt(48));
      System.out.println(salt);
      Status s1 = null;
      try {
         s1 = jtwit.updateStatus(salt + " Hello to you shiny open source people");
      }
      catch (final TwitterException.Timeout e) {
         // identi.ca has problems
      }
      final Status s2 = jtwit.getStatus();
      assert s1.equals(s2) : s1 + " vs " + s2;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getFollowerIDs()} and
    * {@link winterwell.jtwitter.Twitter#getFollowerIDs(String)}.
    * 
    */
   public void testFollowerIDs() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Long> ids = tw.getFollowerIDs();
      for (final Long id : ids) {
         // Getting a 403 Forbidden error here - not sure what that means
         // user id = 33036740 is causing the problem
         // possibly to do with protected updates?
         try {
            assert tw.isFollower(id.toString(), TwitterTest.TEST_USER) : id;
         }
         catch (final E403 e) {
            // this seems to be a corner issue with Twitter's API rather than a bug in JTwitter
            System.out.println(id + " " + e);
         }
      }
      final List<Long> ids2 = tw.getFollowerIDs(TwitterTest.TEST_USER);
      assert ids.equals(ids2);
   }


   /**
    * Test the new cursor-based follower/friend methods.
    */
   public void testManyFollowerIDs() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      tw.setMaxResults(50000);
      final List<Long> ids = tw.getFollowerIDs("stephenfry");
      assertTrue(ids.size() >= 50000);
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getFriendIDs()} and
    * {@link winterwell.jtwitter.Twitter#getFriendIDs(String)}.
    */
   public void testFriendIDs() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Long> ids = tw.getFriendIDs();
      for (final Long id : ids) {
         try {
            assert tw.isFollower(TwitterTest.TEST_USER, id.toString());
         }
         catch (final E403 e) {
            // ignore
            e.printStackTrace();
         }
      }
      final List<Long> ids2 = tw.getFriendIDs(TwitterTest.TEST_USER);
      assert ids.equals(ids2);
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getDirectMessages()}.
    */
   public void testGetDirectMessages() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Message> msgs = tw.getDirectMessages();
      for (final Message message : msgs) {
         assert message.getRecipient().equals(new User(TwitterTest.TEST_USER));
      }
      assert msgs.size() != 0;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getDirectMessagesSent()}.
    */
   public void testGetDirectMessagesSent() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Message> msgs = tw.getDirectMessagesSent();
      for (final Message message : msgs) {
         assert message.getSender().equals(new User(TwitterTest.TEST_USER));
      }
      assert msgs.size() != 0;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getFeatured()}.
    */
   public void testGetFeatured() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<User> f = tw.getFeatured();
      assert f.size() > 0;
      assert f.get(0).status != null;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getFollowers()}.
    */
   public void testGetFollowers() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<User> f = tw.getFollowers();
      assert f.size() > 0;
      assert Twitter.getUser("winterstein", f) != null;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getFriends()}.
    */
   public void testGetFriends() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<User> friends = tw.getFriends();
      assert friends != null;
   }


   /**
    * Test the cursor-based API for getting many followers. Slightly intermittent
    */
   public void testGetManyFollowers() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      tw.setMaxResults(10000); // we don't want to run the test for ever.
      final String victim = "psychovertical";
      final User user = tw.getUser(victim);
      assertFalse("More than 10000 followers; choose a different victim or increase the maximum results",
               user.followersCount > 10000);
      final Set<User> followers = new HashSet(tw.getFollowers(victim));
      final Set<Long> followerIDs = new HashSet(tw.getFollowerIDs(victim));
      // psychovertical has about 600 followers, as of 14/12/09
      assertEquals(user.followersCount, followers.size());
      assertEquals(user.followersCount, followerIDs.size());
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getFriends(java.lang.String)}.
    */
   public void testGetFriendsString() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<User> friends = tw.getFriends("winterstein");
      assert friends != null;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getFriendsTimeline()}.
    */
   public void testGetFriendsTimeline() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Status> ft = tw.getFriendsTimeline();
      assert ft.size() > 0;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getPublicTimeline()}.
    */
   public void testGetPublicTimeline() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Status> pt = tw.getPublicTimeline();
      assert pt.size() > 5;
   }


   public void testGetRateLimitStats() throws InterruptedException {
      {
         final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
         final int i = tw.getRateLimitStatus();
         if (i < 1) {
            return;
         }
         tw.getStatus();
         Thread.sleep(1000);
         final int i2 = tw.getRateLimitStatus();
         assert i - 1 == i2;
      }
      {
         final Twitter tw = new Twitter();
         final int i = tw.getRateLimitStatus();
      }
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getReplies()}.
    */
   public void testGetReplies() {
      {
         final Matcher m = Status.AT_YOU_SIR.matcher("@dan hello");
         assert m.find();
         m.group(1).equals("dan");
      }
      //		{	// done in code
      //			Matcher m = Status.atYouSir.matcher("dan@email.com hello");
      //			assert ! m.find();
      //		}
      {
         final Matcher m = Status.AT_YOU_SIR.matcher("hello @dan");
         assert m.find();
         m.group(1).equals("dan");
      }

      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Status> r = tw.getReplies();
      for (final Status message : r) {
         final List<String> ms = message.getMentions();
         assert ms.contains(TwitterTest.TEST_USER) : message;
      }
      System.out.println("Replies " + r);
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getStatus(int)}.
    */
   public void testGetStatus() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final Status s = tw.getStatus();
      assert s != null;
      System.out.println(s);

      //		// test no status
      //		tw = new Twitter(ANOther Account);
      //		s = tw.getStatus();
      //		assert s == null;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getStatus(long)}.
    */
   public void testGetStatusLong() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final Status s = tw.getStatus();
      final Status s2 = tw.getStatus(s.getId());
      assert s.text.equals(s2.text) : "Fetching a status by id should yield correct text";
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getUserTimeline()}.
    */
   public void testGetUserTimeline() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Status> ut = tw.getUserTimeline();
      assert ut.size() > 0;
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#getUserTimeline(java.lang.String, java.lang.Integer, java.util.Date)}.
    */
   public void testGetUserTimelineString() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final List<Status> ns = tw.getUserTimeline("narrator");
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#isFollower(String)} and
    * {@link winterwell.jtwitter.Twitter#isFollower(String, String)}.
    */
   public void testIsFollower() throws InterruptedException {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);

      assert tw.isFollower("winterstein");
      final int LAG = 5000;
      final User u = tw.stopFollowing("winterstein");
      Thread.sleep(LAG);
      assert !tw.isFollowing("winterstein");
      tw.follow("winterstein");
      Thread.sleep(LAG);
      assert tw.isFollowing("winterstein");
   }


   public void testRetweet() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final String[] tweeps = new String[] { "winterstein", "joehalliwell", "spoonmcguffin", "forkmcguffin" };
      final Status s = tw.getStatus(tweeps[new Random().nextInt(tweeps.length)]);
      final Status rt1 = tw.retweet(s);
      assert rt1.text.contains(s.text) : rt1 + " vs " + s;
      final Status s2 = tw.getStatus("joehalliwell");
      final Status rt2 = tw.updateStatus("RT @" + s2.user.screenName + " " + s2.text);
      assert rt2.text.contains(s2.text) : rt2;
   }


   public void testSearch() {
      {
         final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
         final List<Status> javaTweets = tw.search("java");
         assert javaTweets.size() != 0;
      }
      { // few results
         final Twitter tw = new Twitter();
         tw.setMaxResults(10);
         final List<Status> tweets = tw.search(":)");
         assert tweets.size() == 10;
      }
      { // Lots of results
         final Twitter tw = new Twitter();
         tw.setMaxResults(300);
         final List<Status> tweets = tw.search(":)");
         assert tweets.size() > 100 : tweets.size();
      }
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#sendMessage(java.lang.String, java.lang.String)}.
    */
   public void testSendMessage() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final Message sent = tw.sendMessage("winterstein", "Please ignore this message");
      System.out.println("" + sent);
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#show(java.lang.String)}.
    */
   public void testShow() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final User show = tw.show(TwitterTest.TEST_USER);
      assert show != null;

      // a protected user
      final User ts = tw.show("tassosstevens");
   }


   /**
    * Test method for {@link winterwell.jtwitter.Twitter#updateStatus(java.lang.String)}.
    */
   public void testUpdateStatus() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final String s = "Experimenting (http://winterwell.com at " + new Date().toString() + ")";
      final Status s2a = tw.updateStatus(s);
      final Status s2b = tw.getStatus();
      assert s2b.text.equals(s) : s2b.text;
      assert s2a.id == s2b.id;
      //		assert s2b.source.equals("web") : s2b.source;
   }


   /**
    * This crashes out at above 140, which is correct
    * 
    * @throws InterruptedException
    */
   public void testUpdateStatusLength() throws InterruptedException {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final Random rnd = new Random();
      { // WTF?!
         final Status s2a = tw.updateStatus("Test tweet aaaa " + rnd.nextInt(1000));
      }
      final String salt = new Random().nextInt(1000) + " ";
      Thread.sleep(1000);
      { // well under
         String s = salt + "help help ";
         for (int i = 0; i < 2; i++) {
            s += rnd.nextInt(1000);
            s += " ";
         }
         System.out.println(s.length());
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2b.text.equals(s.trim()) : s2b.text;
         assert s2a.id == s2b.id;
      }
      { // 130
         String s = salt;
         for (int i = 0; i < 12; i++) {
            s += repeat((char) ('a' + i), 9);
            s += " ";
         }
         s = s.trim();
         System.out.println(s.length());
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2b.text.equals(s) : s2b.text;
         assert s2a.id == s2b.id;
      }
      { // 140
         String s = salt;
         for (int i = 0; i < 13; i++) {
            s += repeat((char) ('a' + i), 9);
            s += " ";
         }
         s = s.trim();
         System.out.println(s.length());
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2b.text.equals(s) : s2b.text;
         assert s2a.id == s2b.id;
      }
      // uncomment if you wish to test longer statuses
      if (true) {
         return;
      }
      { // 150
         String s = salt;
         for (int i = 0; i < 14; i++) {
            s += repeat((char) ('a' + i), 9);
            s += " ";
         }
         s = s.trim();
         System.out.println(s.length());
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2b.text.equals(s) : s2b.text;
         assert s2a.id == s2b.id;
      }
      { // 160
         String s = salt;
         for (int i = 0; i < 15; i++) {
            s += repeat((char) ('a' + i), 9);
            s += " ";
         }
         s = s.trim();
         System.out.println(s.length());
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2b.text.equals(s) : s2b.text;
         assert s2a.id == s2b.id;
      }
      { // 170
         String s = salt;
         for (int i = 0; i < 16; i++) {
            s += repeat((char) ('a' + i), 9);
            s += " ";
         }
         s = s.trim();
         System.out.println(s.length());
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2b.text.equals(s) : s2b.text;
         assert s2a.id == s2b.id;
      }

   }


   private String repeat(final char c,
                         final int i) {
      String s = "";
      for (int j = 0; j < i; j++) {
         s += c;
      }
      return s;
   }


   public void testUpdateStatusUnicode() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      {
         final String s = "Katten är hemma. Hur mår du? お元気ですか";
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2b.text.equals(s) : s2b.text;
         assert s2a.id == s2b.id;
      }
      {
         final String s = new Random().nextInt(1000) + " Гладыш Владимир";
         final Status s2a = tw.updateStatus(s);
         final Status s2b = tw.getStatus();
         assert s2a.text.equals(s) : s2a.text;
         assert s2b.text.equals(s) : s2b.text;
         assert s2a.id == s2b.id;
      }
   }


   public void testUserExists() {
      final Twitter tw = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      assert tw.userExists("spoonmcguffin") : "There is a Spoon, honest";
      assert !tw.userExists("chopstickmcguffin") : "However, there is no Chopstick";
      assert !tw.userExists("Alysha6822") : "Suspended users show up as nonexistent";
   }


   /**
    * Created on a day when Twitter's followers API was being particularly flaky, in order to find out just how bad the lag was.
    * 
    * @author miles
    * @throws IOException
    *            if the output file can't be opened for writing
    * @throws InterruptedException
    * 
    */
   public void dontTestFollowLag() throws IOException, InterruptedException {
      final Twitter jt = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      final String spoon = "spoonmcguffin";
      final long timestamp = (new Date()).getTime();
      final FileWriter outfile = new FileWriter("twitlag" + timestamp + ".txt");
      for (int i = 0; i < 1000; i++) {
         System.out.println("Starting iteration " + i);
         try {
            if (jt.isFollowing(spoon)) {
               System.out.println("jtwit was following Spoon");
               jt.stopFollowing(spoon);
               int counter = 0;
               while (jt.isFollowing(spoon)) {
                  Thread.sleep(1000);
                  // jt.stopFollowing(spoon);
                  counter++;
               }
               try {
                  outfile.write("Stopped following: " + counter + "00ms\n");
               }
               catch (final IOException e) {
                  System.out.println("Couldn't write to file: " + e);
               }
            }
            else {
               System.out.println("jtwit was not following Spoon");
               jt.follow(spoon);
               int counter = 0;
               while (!jt.isFollowing(spoon)) {
                  Thread.sleep(1000);
                  // jt.follow(spoon);
                  counter++;
               }
               try {
                  outfile.write("Started following: " + counter + "00ms\n");
               }
               catch (final IOException e) {
                  System.out.println("Couldn't write to file: " + e);
               }
            }
         }
         catch (final E403 e) {
            System.out.println("isFollower() was mistaken: " + e);
         }
         outfile.flush();
      }
      outfile.close();
   }


   /**
	 *
	 */
   public void testIsValidLogin() {
      Twitter twitter = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      assertTrue(twitter.isValidLogin());
      twitter = new Twitter("rumpelstiltskin", "thisisnotarealpassword");
      assertFalse(twitter.isValidLogin());
   }


   public void testIdentica() {
      final Twitter twitter = new Twitter(TwitterTest.TEST_USER, TwitterTest.TEST_PASSWORD);
      twitter.setAPIRootUrl("http://identi.ca/api");
      twitter.setStatus("Testing jTwitter http://winterwell.com/software/jtwitter.php");
   }

}
