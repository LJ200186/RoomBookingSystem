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
public class Booking {
    
    public int BookingID;
    public String BookingType;
    public int BookingHostID;
    public int BookingRoomID;
    public long BookingStartTime;
    public long BookingEndTime;
    public String Booking_RefreshmentsRequested;
    public long Booking_RefreshmentsDeliveryTime;

    public Booking(int BookingID, String BookingType, int BookingHostID,int BookingRoomID, long BookingStartTime, long BookingEndTime, String Booking_RefreshmentsRequested, long Booking_RefreshmentsDeliveryTime) {
        this.BookingID = BookingID;
        this.BookingType = BookingType;
        this.BookingHostID = BookingHostID;
        this.BookingRoomID = BookingRoomID;
        this.BookingStartTime = BookingStartTime;
        this.BookingEndTime = BookingEndTime;
        this.Booking_RefreshmentsRequested = Booking_RefreshmentsRequested;
        this.Booking_RefreshmentsDeliveryTime = Booking_RefreshmentsDeliveryTime;
    }
    
    
    
}
