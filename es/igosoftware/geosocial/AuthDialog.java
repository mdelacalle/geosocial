package es.igosoftware.geosocial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import winterwell.jtwitter.Twitter;

public class AuthDialog
         extends
            JDialog {

   private Twitter           _twit;

   /**
    * 
    */
   private static final long serialVersionUID = 3469280231397983735L;
   private final JLabel      _result          = new JLabel("Insert User&Password");


   public Twitter getTwitterObject() {
      return _twit;
   }


   public AuthDialog() {
      final JPanel autPanel = new JPanel();
      autPanel.setLayout(new BorderLayout());
      autPanel.setSize(new Dimension(500, 50));

      try {

         final Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
         final Method mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
         mSetWindowOpacity.invoke(null, this, Float.valueOf(0.85f));
         this.setUndecorated(true);
         this.setBackground(new Color(193, 222, 238));


         final JPanel panelTexts = new JPanel();

         final JLabel userl = new JLabel("User:");
         panelTexts.add(userl);

         final JTextField userT = new JTextField(15);

         //TODO:quitar
         userT.setText("geoigotwit");

         panelTexts.add(userT);
         final JLabel passl = new JLabel("Password:");
         panelTexts.add(passl);
         final JPasswordField passT = new JPasswordField(15);

         //TODO:quitar
         passT.setText("hola12");
         panelTexts.add(passT);


         final JPanel panelButtons = new JPanel();
         final ImageIcon ii = new ImageIcon(new URL("http://a0.twimg.com/a/1269553143/images/twitter_logo_header.png"));
         final ImageIcon iiScaled = new ImageIcon(ii.getImage().getScaledInstance(86, 20, Image.SCALE_FAST));
         final JButton authenticate = new JButton("Authenticate!", iiScaled);


         authenticate.addActionListener(new ActionListener() {


            @SuppressWarnings("deprecation")
            @Override
            public void actionPerformed(final ActionEvent e) {

               _twit = new Twitter(userT.getText(), passT.getText());

               if (_twit.isValidLogin()) {
                  _result.setText("correct login");
                  setVisible(false);
               }
               else {
                  _result.setText("No login!, Try again");
                  AuthDialog.this.repaint();
               }

            }
         });


         final ImageIcon iiClose = new ImageIcon(new URL(
                  "http://www.iconarchive.com/icons/deleket/sleek-xp-basic/256/Close-icon.png"));
         final ImageIcon iiCloseScaled = new ImageIcon(iiClose.getImage().getScaledInstance(20, 20, Image.SCALE_FAST));
         final JButton close = new JButton("Exit", iiCloseScaled);

         close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
               System.exit(0);
            }
         });


         panelButtons.add(authenticate);
         panelButtons.add(close);


         autPanel.add(panelTexts, BorderLayout.NORTH);
         autPanel.add(panelButtons, BorderLayout.CENTER);


         final JPanel panelResults = new JPanel();
         panelResults.add(_result);

         autPanel.add(panelResults, BorderLayout.SOUTH);

      }
      catch (final MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final ClassNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final SecurityException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final NoSuchMethodException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final IllegalArgumentException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final IllegalAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final InvocationTargetException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }


      this.add(autPanel);
   }

}
