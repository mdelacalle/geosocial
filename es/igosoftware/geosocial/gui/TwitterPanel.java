package es.igosoftware.geosocial.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.Twitter.User;


public class TwitterPanel
         extends
            JPanel {

   /**
    * 
    */
   private static final long serialVersionUID = -3392299729825951667L;
   Image                     image;
   private JPanel            _userPanel;


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


         _userPanel = new JPanel();
         _userPanel.setBackground(new Color(0, 0, 0, 0));
         _userPanel.setLayout(new FlowLayout());


         final ImageIcon iiProfileScaled = new ImageIcon(
                  (new ImageIcon(user.getProfileImageUrl().toURL()).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));

         final JLabel img = new JLabel(iiProfileScaled);
         _userPanel.add(img);

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


         _userPanel.add(screenNamePanel);
         this.add(_userPanel);
      }


      catch (final MalformedURLException e) {
         System.out.println("User Photo Exception!");
      }

   }


   public void refreshTwits(final Twitter tw) {

      final JPanel statusPanel = new JPanel();
      statusPanel.setLayout(new VerticalLayout());
      statusPanel.setBackground(new Color(0, 0, 0, 0));

      final List<Status> statuses = tw.getHomeTimeline();
      final int i = 0;
      for (final Status status : statuses) {
         statusPanel.add(getMessagePanel(status));
      }

      final JScrollPane scroll = new JScrollPane(statusPanel);
      scroll.setBackground(new Color(0, 0, 0, 0));

      scroll.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - _userPanel.getHeight()));
      scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


      this.add(scroll);
   }


   private Component getMessagePanel(final Status status) {

      final JPanel messagePanel = new JPanel();
      messagePanel.setLayout(new FlowLayout());
      messagePanel.setBackground(new Color(0, 0, 0, 0));
      messagePanel.setBorder(BorderFactory.createLineBorder(Styles.blueDarkTwitter));

      //     try {


      final User user = status.getUser();


      //         final ImageIcon iiProfileScaled = new ImageIcon(
      //                  (new ImageIcon(user.getProfileImageUrl().toURL()).getImage().getScaledInstance(25, 25, Image.SCALE_FAST)));
      //
      //
      //         final JLabel img = new JLabel(iiProfileScaled);
      //         messagePanel.add(img);

      final JPanel textsPanel = new JPanel(new VerticalLayout());
      textsPanel.setBackground(new Color(0, 0, 0, 0));
      final JLabel userLabel = new JLabel("@" + user.getScreenName());
      //      userLabel.setFont(Styles.font9Bold);
      //      userLabel.setForeground(Styles.blueDarkTwitter);

      textsPanel.add(userLabel);

      final JTextArea testLabel = new JTextArea(status.getText(), 5, 25);
      testLabel.setBackground(Styles.blueTwitter);
      testLabel.setAutoscrolls(false);
      testLabel.setLineWrap(true);
      testLabel.setWrapStyleWord(true);
      testLabel.setEditable(false);
      testLabel.setFont(Styles.font9);
      textsPanel.add(testLabel);

      messagePanel.add(textsPanel);

      //      }
      //      catch (final MalformedURLException e) {
      //         System.out.println("User Photo Exception!");
      //      }


      return messagePanel;
   }
}
