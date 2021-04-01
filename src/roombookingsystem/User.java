/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roombookingsystem;

/**
 *
 * @author ldjet
 */
public class User {
    
    public int UserID;
    public String UserEmail;
    public String UserPWordHash;
    public String UserType;

    public User(int UserID, String UserEmail, String UserPWordHash, String UserType) {
        this.UserID = UserID;
        this.UserEmail = UserEmail;
        this.UserPWordHash = UserPWordHash;
        this.UserType = UserType;
    }
    
    
}
