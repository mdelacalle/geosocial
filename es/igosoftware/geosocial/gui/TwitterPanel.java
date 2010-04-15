package es.igosoftware.geosocial.gui;

import es.igosoftware.geosocial.Config;
import es.igosoftware.geosocial.geo.Geocoding;
import es.igosoftware.geosocial.geo.SimbologyRenderer;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

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
   WorldWindowGLCanvas       _wwd;


   public TwitterPanel() {
      try {

         System.out.println(Config.imgPath + "bgtw.png");
         image = javax.imageio.ImageIO.read(new URL(Config.imgPath + "bgtw.png"));
         this.setLayout(new VerticalLayout());
         this.setDoubleBuffered(true);

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


   public void refreshTwits(final Twitter tw,
                            final WorldWindowGLCanvas wwd) {

      _wwd = wwd;
      final JLabel label = new JLabel("loading twits...");
      this.add(label);

      final SwingWorker worker = new SwingWorker() {

         @Override
         protected JScrollPane doInBackground() throws Exception {
            final JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new VerticalLayout());
            statusPanel.setBackground(new Color(0, 0, 0, 0));

            final List<Status> statuses = tw.getHomeTimeline();


            final ArrayList<String> positions = new ArrayList<String>();
            for (final Status status : statuses) {
               statusPanel.add(getMessagePanel(status));
               final String position = status.getGeo();
               if (position == null) {
                  positions.add(Geocoding.getCoordinates(status.getUser().getLocation()));
               }
               else {
                  positions.add(position);
               }
            }
            SimbologyRenderer.renderPositions(positions, _wwd);
            final JScrollPane scroll = new JScrollPane(statusPanel);

            scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

               @Override
               public void adjustmentValueChanged(final AdjustmentEvent e) {
                  TwitterPanel.this.repaint();
               }
            });

            scroll.setBackground(new Color(0, 0, 0, 0));
            scroll.setPreferredSize(new Dimension(TwitterPanel.this.getWidth(), TwitterPanel.this.getHeight()
                                                                                - _userPanel.getHeight()));
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

            TwitterPanel.this.remove(label);
            TwitterPanel.this.setVisible(false);
            TwitterPanel.this.setVisible(true);
            return scroll;

         }

      };
      worker.run();

      try {
         this.add((JScrollPane) worker.get());
      }
      catch (final InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final ExecutionException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }


   }


   private Component getMessagePanel(final Status status) {

      final JPanel messagePanel = new JPanel();
      messagePanel.setLayout(new VerticalLayout());
      messagePanel.setBackground(new Color(0, 0, 0, 0));
      messagePanel.setBorder(BorderFactory.createLineBorder(Styles.blueDarkTwitter));

      try {


         final User user = status.getUser();


         final ImageIcon iiProfileScaled = new ImageIcon(
                  (new ImageIcon(user.getProfileImageUrl().toURL()).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));


         final JLabel img = new JLabel("@" + user.getScreenName(), iiProfileScaled, SwingConstants.LEFT);
         img.setForeground(Styles.blueDarkTwitter);
         img.setFont(Styles.font12Bold);

         img.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(final MouseEvent e) {}


            @Override
            public void mousePressed(final MouseEvent e) {}


            @Override
            public void mouseExited(final MouseEvent e) {}


            @Override
            public void mouseEntered(final MouseEvent e) {}


            @Override
            public void mouseClicked(final MouseEvent e) {
               System.out.println("Show the User Profile");
            }
         });

         messagePanel.add(img);

         final JPanel textsPanel = new JPanel(new VerticalLayout());
         textsPanel.setBackground(new Color(0, 0, 0, 0));


         final JEditorPane testLabel = new JEditorPane();
         testLabel.setContentType("text/html");


         testLabel.setText("<html><br><span style=\"font-family:monospace; font-size: 9px ;\">" + status.getText()
                           + "</span>  <a href=\"http://www.igosoftware.es\"> &nbsp; igo</a></html>");
         // testLabel.setBackground(Styles.blueTwitter);
         testLabel.setBackground(new Color(0, 0, 0, 0));
         testLabel.setAutoscrolls(false);
         testLabel.setPreferredSize(new Dimension(177, 88));
         testLabel.setEditable(false);
         testLabel.setFont(Styles.font9);

         testLabel.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(final HyperlinkEvent e) {

               if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                  System.out.println(e.getURL());
               }

            }
         });

         textsPanel.add(testLabel);
         messagePanel.add(textsPanel);
      }
      catch (final MalformedURLException e) {
         System.out.println("User Photo Exception!");
      }
      return messagePanel;
   }
}
