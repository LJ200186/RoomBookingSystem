/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roombookingsystem;

import java.security.MessageDigest;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author ldjet
 */
public class RoomBookingSystem extends Application {
    
    static public User LoggedInUser;
    
    static private Stage mainStage;

    static private Random random = new Random();
    
    public static void loadPartnerPanel(User ToBeLoggedInUser){
        try {
            Scene PartnerPageScene = new Scene(FXMLLoader.load(RoomBookingSystem.class.getResource("PartnerPage.fxml")));
            
            mainStage.setScene(PartnerPageScene);
            
            LoggedInUser = ToBeLoggedInUser;
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static boolean SendEmail(String Subject, String Content, String Email) {

        String to = Email;

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "mail.gaming.bz");
        properties.setProperty("mail.user", "hello@luk3.uk");
        properties.setProperty("mail.password", System.getenv("MAIL_PASSWORD"));
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hello@luk3.uk", "JavaMail");
            }
        };

        Session session = Session.getDefaultInstance(properties, auth);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("hello@luk3.uk"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(Subject);
            message.setText(Content);

            // Send message
            Transport.send(message);
            System.out.println("Email sent");
            return true;
        } catch (MessagingException mex) {
            return false;
        }
    }

    public static String hashPassword(String ToHash) {
        try {
            
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(ToHash.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            String hashedPassword = sb.toString();
            
            return(hashedPassword);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static int getRandom(int Digits) {

        return random.nextInt(Digits);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Scene WelcomePageScene = new Scene(FXMLLoader.load(getClass().getResource("WelcomePage.fxml")));

        stage.setTitle("Room Booking System");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        
        mainStage = stage;

        //WelcomePage.LoginB.setText();
        //Scene WelcomePageScene = new Scene(FXMLLoader.load(getClass().getResource("WelcomePage.fxml")));
        stage.setScene(WelcomePageScene);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
