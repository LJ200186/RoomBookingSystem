/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roombookingsystem;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javax.mail.internet.*;

/**
 * FXML Controller class
 *
 * @author ldjet
 */
public class WelcomePageController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String CurDate = dateFormat.format(date);
        
        DateLabel.setText(CurDate);

    } 
    
    public Pane ButtonsPane;
    public Pane LoginPane;
    public Pane RegisterPane;
    public Pane EmailValidationPane;
    public Pane AccountTypePane;
    
    public Label DateLabel;
    public Label RegisterStatusLabel;
    public Label LoginStatusLabel;
    public Label EmailValidationStatusLabel;
    
    public TextField LoginEmailEntryField;
    public TextField RegisterEmailEntryField;
    public TextField EmailValidationEntryField;
    
    public ProgressIndicator EmailValidationLoading;
    public ComboBox AccountTypeChoiceBox;
    
    public PasswordField LoginPasswordEntryField;
    public PasswordField RegisterPasswordEntryField;
    
    private int ConfirmCode;
    private String[] NewUserInfo = new String[3];

    @FXML
    private void LoginBAction(){
        System.out.println("Login Pressed");
        
        ButtonsPane.setVisible(false);
        RegisterPane.setVisible(false);
        LoginPane.setVisible(true);
        Platform.runLater( () -> LoginPane.requestFocus() );
        
    }
    
    private boolean CheckEmailValidation(String Email){
        
        try{
            InternetAddress emailAddr = new InternetAddress(Email);
            emailAddr.validate();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    @FXML
    private void EmailConfirmation(){
        if(EmailValidationEntryField.getText().equals(Integer.toString(ConfirmCode))){
            EmailValidationPane.setVisible(false);
            AccountTypeChoiceBox.getItems().add("Partner");
            AccountTypeChoiceBox.getItems().add("Caterer");
            AccountTypeChoiceBox.getItems().add("Cleaner");
            AccountTypePane.setVisible(true);
            
        }else{
            EmailValidationStatusLabel.setText("Error: Invalid confirmation code");
        }
    }
    
    @FXML
    private void ConfirmAccountType(){
        String AccountType = (String) AccountTypeChoiceBox.getValue();
        System.out.println(AccountType);
        
        if(AccountType != null){
            DatabaseFunctions.createUser(NewUserInfo[0], NewUserInfo[1], AccountType);
            System.out.println("Account Created");
            AccountTypePane.setVisible(false);
            LoginPane.setVisible(true);
        }
    }
    
    @FXML
    private void Login(){
        
        LoginStatusLabel.setText(" ");
        String EnteredEmail = LoginEmailEntryField.getText();
        String EnteredPassword = LoginPasswordEntryField.getText();
        String HashedEnteredPassword = RoomBookingSystem.hashPassword(EnteredPassword);
        
        User EnteredUser = DatabaseFunctions.getUser(EnteredEmail);
        
        if(EnteredUser == null){
            LoginStatusLabel.setText("Error: Account not found");
        }

        if(EnteredUser.UserPWordHash.equals(HashedEnteredPassword)){
            if("Partner".equals(EnteredUser.UserType)){
                RoomBookingSystem.loadPartnerPanel(EnteredUser);
            }
            
        }else{
            LoginStatusLabel.setText("Error: Incorrect password");
        }
    }
    
    private void SendComfirmationEmail(String EnteredEmail){
        ConfirmCode = RoomBookingSystem.getRandom(999999);
            
        RoomBookingSystem.SendEmail("Confirm your email address", "Thanks for signing up!\n\nYour confirmation code is: "+ConfirmCode+"\n\nEnter this code into the program to finish setting up your account!", EnteredEmail);
    }
     
    
    @FXML
    private void Register(){
        
        String EnteredEmail = RegisterEmailEntryField.getText();
        String EnteredPassword = RegisterPasswordEntryField.getText();
        
        boolean EmailValid = CheckEmailValidation(EnteredEmail);
        
        if(EmailValid){
            
            System.out.println("Valid");
            
            EmailValidationPane.setVisible(true);
            RegisterPane.setVisible(false);
            
            CompletableFuture.runAsync(() -> SendComfirmationEmail(EnteredEmail));
            
            NewUserInfo[0] = EnteredEmail;
            NewUserInfo[1] = RoomBookingSystem.hashPassword(EnteredPassword);
            
        }else{
            RegisterStatusLabel.setText("Error: Invalid Email Address");
            return;
        }
    }
    
    @FXML
    private void RegisterBAction(){
        System.out.println("Register Pressed");
       
        ButtonsPane.setVisible(false);
        RegisterPane.setVisible(true);
        LoginPane.setVisible(false);
        Platform.runLater( () -> RegisterPane.requestFocus() );
        
    }
    
}
