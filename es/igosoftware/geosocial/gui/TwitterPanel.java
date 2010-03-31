package es.igosoftware.geosocial.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.User;


public class TwitterPanel
         extends
            JPanel {

   /**
    * 
    */
   private static final long serialVersionUID = -3392299729825951667L;
   Image                     image;


   public TwitterPanel() {
      try {
         image = javax.imageio.ImageIO.read(new File("img/bgtw.png"));
         this.setLayout(new VerticalLayout());

      }
      catch (final Exception e) { /*handled in paintComponent()*/}
   }


   @Override
   protected void paintComponent(final Graphics g) {
      super.paintComponent(g);
      if (image != null) {
         g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
      }
      else {
         System.out.println("image is null");
      }
   }


   public void setUser(final User user) {

      try {


         final JPanel userPanel = new JPanel();
         userPanel.setBackground(new Color(0, 0, 0, 0));
         userPanel.setLayout(new FlowLayout());


         final ImageIcon iiProfileScaled = new ImageIcon(
                  (new ImageIcon(user.getProfileImageUrl().toURL()).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));

         final JLabel img = new JLabel(iiProfileScaled);
         userPanel.add(img);

         final JPanel screenNamePanel = new JPanel();
         screenNamePanel.setLayout(new BorderLayout());
         screenNamePanel.setBackground(new Color(0, 0, 0, 0));

         screenNamePanel.add(new JLabel("  " + user.getScreenName()), BorderLayout.NORTH);


         final JPanel followersPanel = new JPanel();
         followersPanel.setBackground(new Color(0, 0, 0, 0));
         followersPanel.setLayout(new FlowLayout());
         followersPanel.add(new JLabel(Integer.toString(user.getFollowersCount())));
         final JLabel followers = (new JLabel("followers"));
         followers.setFont(Styles.font10);
         followersPanel.add(followers);
         followersPanel.add(new JLabel(Integer.toString(user.getFriendsCount())));
         final JLabel friends = (new JLabel("friends"));
         friends.setFont(Styles.font10);
         followersPanel.add(friends);

         screenNamePanel.add(followersPanel, BorderLayout.CENTER);


         userPanel.add(screenNamePanel);
         this.add(userPanel);
      }


      catch (final MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   public void refreshTwits(final Twitter tw) {

      final JScrollPane scroll = new JScrollPane();
      scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

      for (int i = 0; i < 50; i++) {

         scroll.add(new JLabel("soy el label:" + i));

      }


      this.add(scroll);
   }
}
