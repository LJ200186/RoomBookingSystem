/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roombookingsystem;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PartnerPageController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String CurDate = dateFormat.format(date);

        DateLabel.setText(CurDate);

        TimeSlots.put("am0800", am0800);
        TimeSlots.put("am0830", am0830);
        TimeSlots.put("am0900", am0900);
        TimeSlots.put("am0930", am0930);
        TimeSlots.put("am1000", am1000);
        TimeSlots.put("am1030", am1030);
        TimeSlots.put("am1100", am1100);
        TimeSlots.put("am1130", am1130);
        TimeSlots.put("pm1200", pm1200);
        TimeSlots.put("pm1230", pm1230);
        TimeSlots.put("pm1300", pm1300);
        TimeSlots.put("pm1330", pm1330);
        TimeSlots.put("pm1400", pm1400);
        TimeSlots.put("pm1430", pm1430);
        TimeSlots.put("pm1500", pm1500);
        TimeSlots.put("pm1530", pm1530);
        TimeSlots.put("pm1600", pm1600);
        TimeSlots.put("pm1630", pm1630);

        BookingRoomChoiceComboBox.getItems().addAll("1", "2", "3", "4", "5");

        ObservableList<String> StartTimeList = FXCollections.observableArrayList();
        ObservableList<String> EndTimeList = FXCollections.observableArrayList();

        for (Enumeration e = TimeSlots.keys(); e.hasMoreElements();) {
            String TimeSlotSTR = (String) e.nextElement();
            String ReFormattedTime = TimeSlotSTR.substring(2, 4) + ":" + TimeSlotSTR.substring(4, 6);
            StartTimeList.add(ReFormattedTime);
            EndTimeList.add(ReFormattedTime);
        }

        EndTimeList.remove("08:00");
        EndTimeList.add("17:00");

        Collections.sort(StartTimeList);
        Collections.sort(EndTimeList);

        BookingStartTimeComboBox.setItems(StartTimeList);
        BookingEndTimeComboBox.setItems(EndTimeList);

    }

    private Dictionary TimeSlots = new Hashtable();
    
    private Booking NewBooking;

    public Label am0800;
    public Label am0830;
    public Label am0900;
    public Label am0930;
    public Label am1000;
    public Label am1030;
    public Label am1100;
    public Label am1130;
    public Label pm1200;
    public Label pm1230;
    public Label pm1300;
    public Label pm1330;
    public Label pm1400;
    public Label pm1430;
    public Label pm1500;
    public Label pm1530;
    public Label pm1600;
    public Label pm1630;
    public Label DateLabel;
    public Label BookingStatusLabel;

    public Pane MenuPane;
    public Pane CreateBookingScrollPanePane;

    public ComboBox BookingRoomChoiceComboBox;
    public ComboBox BookingStartTimeComboBox;
    public ComboBox BookingEndTimeComboBox;
    public DatePicker BookingDateSelection;

    private ArrayList<Booking> sortBookings(long Start, long End, ArrayList<Booking> RoomBookings) {

        for (int i = 0; i < RoomBookings.size(); i++) {
            Booking CurrentBooking = RoomBookings.get(i);
            if (!((CurrentBooking.BookingStartTime >= Start) && (CurrentBooking.BookingEndTime <= End))) {
                RoomBookings.remove(CurrentBooking);
            }
        }
        return (RoomBookings);
    }

    private Label getTimeSlot(long epoch) {

        Date date = new Date(epoch);
        DateFormat format = new SimpleDateFormat("aakkmm");

        String FormattedTime = format.format(date);
        return ((Label) TimeSlots.get(FormattedTime.toLowerCase()));
    }

    private int isActiveBooking(ArrayList<Booking> RoomBookings, long epoch) {
        for (Booking CurrentBooking : RoomBookings) {
            if ((CurrentBooking.BookingStartTime <= epoch) && (CurrentBooking.BookingEndTime >= epoch)) {
                if (CurrentBooking.BookingType.equals("CLEANING")) {
                    return 2;
                } else {
                    return 1;
                }
            }

        }
        return 0;
    }

    @FXML
    private void OnContinue() {
        BookingStatusLabel.setText("");
        String SelectedRoom = (String) BookingRoomChoiceComboBox.getValue();
        if ((SelectedRoom != null) && (BookingDateSelection.getValue() != null) && (BookingStartTimeComboBox.getValue() != null) && (BookingEndTimeComboBox.getValue() != null)) {

            String[] StartTime = ((String) BookingStartTimeComboBox.getValue()).split(":");
            LocalDateTime OldStartDateTime = BookingDateSelection.getValue().atTime(Integer.parseInt(StartTime[0]), Integer.parseInt(StartTime[1]));
            Date StartDateTime = Date.from(OldStartDateTime.toInstant(ZoneOffset.UTC));

            String[] EndTime = ((String) BookingEndTimeComboBox.getValue()).split(":");
            LocalDateTime OldEndDateTime = BookingDateSelection.getValue().atTime(Integer.parseInt(EndTime[0]), Integer.parseInt(EndTime[1]));
            Date EndDateTime = Date.from(OldEndDateTime.toInstant(ZoneOffset.UTC));

            boolean timeselectedfree = true;
            ArrayList<Booking> RoomBookings = DatabaseFunctions.getRoomBookings((String) SelectedRoom);
            RoomBookings = sortBookings(StartDateTime.getTime(), EndDateTime.getTime(), RoomBookings);
            
            if(RoomBookings.isEmpty()){

            RoomBookings = sortBookings(EndDateTime.getTime(), EndDateTime.getTime()+ 1800000, DatabaseFunctions.getRoomBookings(SelectedRoom));
                
            if(RoomBookings.isEmpty()){
                NewBooking = new Booking(-1, "Meeting", RoomBookingSystem.LoggedInUser.UserID, Integer.parseInt(SelectedRoom), StartDateTime.getTime(), EndDateTime.getTime(), "", -1);
                
                // SETUP BOOKING EXTRAS PAGE
            }else{
                BookingStatusLabel.setText("Error: No cleaning slot available");
            }
                
                
            }else{
                BookingStatusLabel.setText("Error: Room in use at selected time");
            }

        }else{
            BookingStatusLabel.setText("Error: Invalid data entry");
        }

    }

    @FXML
    private void OnDateRoomSelected() {
        String SelectedRoom = (String) BookingRoomChoiceComboBox.getValue();
        if ((SelectedRoom != null) && (BookingDateSelection.getValue() != null)) { //If both fields have values
            ArrayList<Booking> RoomBookings = DatabaseFunctions.getRoomBookings(SelectedRoom);

            Date SelectedDate = Date.from(BookingDateSelection.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            RoomBookings = sortBookings(SelectedDate.getTime(), SelectedDate.getTime() + 61200000L, RoomBookings);

            for (long i = SelectedDate.getTime() + 28800000; i <= SelectedDate.getTime() + 59400000; i = i + 1800000) {
                int activeBooking = isActiveBooking(RoomBookings, i);
                Label CurrentTimeSlot = getTimeSlot(i);

                if (activeBooking == 1) {
                    CurrentTimeSlot.setText("BUSY");
                    CurrentTimeSlot.setStyle("-fx-text-fill: #e14141");
                } else if (activeBooking == 2) {
                    CurrentTimeSlot.setText("CLEANING");
                    CurrentTimeSlot.setStyle("-fx-text-fill: #342fb9");
                } else {
                    CurrentTimeSlot.setText("FREE");
                    CurrentTimeSlot.setStyle("-fx-text-fill: #42d747");
                }
            }
            CreateBookingScrollPanePane.setVisible(true);
        } else {
            CreateBookingScrollPanePane.setVisible(false);
        }
    }

}
