/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roombookingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class DatabaseFunctions {

    private static final String DatabaseLocation = System.getProperty("user.dir") + "\\RoomBookingSystem.accdb";
    private static Connection con;

// <editor-fold defaultstate="collapsed" desc="Main database functions">    
    public static ResultSet executeQuery(Connection con, String query) {

        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(query);

            return rs;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public static void executeUpdateQuery(Connection con, String query) {

        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(query);

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            return con;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        return null;
    }
//</editor-fold>

    public static boolean createUser(String Email, String HashPWord, String AccountType) {

        try {
            String sql = "INSERT INTO Users(UserEmail,UserPWordHash,UserType) VALUES('" + Email + "','" + HashPWord + "','" + AccountType + "');";
            executeUpdateQuery(getConnection(), sql);
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
    
    public static boolean createBooking(Booking toBook){
        
        try {
            String sql = "INSERT INTO Bookings(BookingType,BookingHostID,BookingRoomID,BookingStartTime,BookingEndTime,Booking_RefreshmentsRequested,Booking_RefreshmentsDeliveryTime,Booking_Resources) VALUES('" + toBook.BookingType + "'," + toBook.BookingHostID + "," + toBook.BookingRoomID + "," + toBook.BookingStartTime + "," + toBook.BookingEndTime + ",'" + toBook.Booking_RefreshmentsRequested + "'," + toBook.Booking_RefreshmentsDeliveryTime +",'" + toBook.Booking_Resources +"')";
            executeUpdateQuery(getConnection(), sql);
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUser(String UserEmail) {

        try {
            String sql = "SELECT * FROM Users where UserEmail = '" + UserEmail + "'";
            ResultSet rs = executeQuery(getConnection(), sql);

            if (rs.next()) {
                User newUser = new User(rs.getInt("UserID"), rs.getString("UserEmail"), rs.getString("UserPWordHash"), rs.getString("UserType"));
                rs.close();
                con.close();
                return newUser;
            }
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        return null;
    }
    
    

    public static ArrayList<Booking> getRoomBookings(String RoomID) {
        try {

            ArrayList<Booking> RoomBookings = new ArrayList<>();

            String sql = "SELECT * FROM Bookings where BookingRoomID = '" + RoomID + "'";
            ResultSet rs = executeQuery(getConnection(), sql);

            while (rs.next()) {
                Booking newBooking = new Booking(rs.getInt("BookingID"),rs.getString("BookingType"),rs.getInt("BookingHostID"),rs.getInt("BookingRoomID"),rs.getLong("BookingStartTime"), rs.getLong("BookingEndTime"), rs.getString("Booking_RefreshmentsRequested"), rs.getLong("Booking_RefreshmentsDeliveryTime"), rs.getString("Booking_Resources"));
                RoomBookings.add(newBooking);
            }
            rs.close();
            con.close();
            return RoomBookings;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
