/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roombookingsystem;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

        RefreshmentComboBox.getItems().addAll("Water", "Coffee/Tea", "Pastry Selection", "Sandwich Selection");
        ResourcesComboBox.getItems().addAll("Projector", "Stationary", "Notepads", "Whiteboard");

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Caught " + e);
                e.printStackTrace();
            }
        }
        );
    }

    private Dictionary TimeSlots = new Hashtable();

    private Booking NewBooking;
    private Booking NewCleaning;

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
    public Label ExtrasStatusLabel;

    public Pane BookingExtrasPane;
    public Pane CreateBookingPane;
    public Pane MenuPane;
    public Pane CreateBookingScrollPanePane;

    public ListView ExtrasListView;
    public ComboBox RefreshmentComboBox;
    public ComboBox ResourcesComboBox;
    public ComboBox RefreshmentTimeComboBox;
    public ComboBox BookingRoomChoiceComboBox;
    public ComboBox BookingStartTimeComboBox;
    public ComboBox BookingEndTimeComboBox;
    public DatePicker BookingDateSelection;

    private ArrayList<Booking> sortBookings(long Start, long End, ArrayList<Booking> RoomBookings) {

        int Size = RoomBookings.size();

        for (int i = 0; i < Size;) {
            Booking CurrentBooking = RoomBookings.get(i);

            boolean isbetweenStart = (Start <= CurrentBooking.BookingStartTime) && (End > CurrentBooking.BookingStartTime);
            boolean isbetweenEnd = (End <= CurrentBooking.BookingEndTime) && (End > CurrentBooking.BookingEndTime);

            if (!(isbetweenStart || isbetweenEnd)) {
                RoomBookings.remove(CurrentBooking);
                Size--;
                if (Size == 0) {
                    return (RoomBookings);
                }
            } else {
                i = i + 1;
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
    private void CreateBookingMenuB() {
        MenuPane.setVisible(false);
        CreateBookingPane.setVisible(true);
    }

    @FXML
    private void ViewScheduleMenuB() {
        MenuPane.setVisible(false);
        //VIEW SCHEDULE PANE VISIBLE
    }

    @FXML
    private void RefreshmentPicked() {
        try {
            ObservableList<String> SelectedExtras = ExtrasListView.getItems();
            String PickedRefreshment = (String) RefreshmentComboBox.getValue();

            if (PickedRefreshment != null) {
                if (SelectedExtras.size() != 0) {
                    if (SelectedExtras.contains(PickedRefreshment)) {
                        SelectedExtras.remove(PickedRefreshment);
                    } else {
                        SelectedExtras.add(PickedRefreshment);
                    }

                } else {
                    SelectedExtras.add(0, PickedRefreshment);
                }
            }

            EventHandler<ActionEvent> handler = RefreshmentComboBox.getOnAction();
            RefreshmentComboBox.setOnAction(null);
            ExtrasListView.setItems(SelectedExtras);
            RefreshmentComboBox.getSelectionModel().clearSelection();
            RefreshmentComboBox.setOnAction(handler);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void ResourcePicked() {
        try {
            ObservableList<String> SelectedExtras = ExtrasListView.getItems();
            String PickedResource = (String) ResourcesComboBox.getValue();

            if (PickedResource != null) {
                if (SelectedExtras.size() != 0) {
                    if (SelectedExtras.contains(PickedResource)) {
                        SelectedExtras.remove(PickedResource);
                    } else {
                        SelectedExtras.add(PickedResource);
                    }

                } else {
                    SelectedExtras.add(0, PickedResource);
                }
            }
            EventHandler<ActionEvent> handler = ResourcesComboBox.getOnAction();
            ResourcesComboBox.setOnAction(null);
            ExtrasListView.setItems(SelectedExtras);
            ResourcesComboBox.getSelectionModel().clearSelection();
            ResourcesComboBox.setOnAction(handler);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    private void OnBook() {

        ExtrasStatusLabel.setText("");

        ArrayList<String> SelectedRefreshments = new ArrayList<>();
        ArrayList<String> SelectedResources = new ArrayList<>();

        if (ExtrasListView.getItems().size() != 0) {

            for (Object ObjCurrentExtra : ExtrasListView.getItems()) {

                String CurrentExtra = (String) ObjCurrentExtra;

                if (RefreshmentComboBox.getItems().contains(CurrentExtra)) {
                    SelectedRefreshments.add(CurrentExtra);
                } else if (ResourcesComboBox.getItems().contains(CurrentExtra)) {
                    SelectedResources.add(CurrentExtra);
                }
            }

            if ((SelectedRefreshments.size() != 0) && (RefreshmentTimeComboBox.getValue() == null)) {
                ExtrasStatusLabel.setText("Error: Set Refreshment delivery time");
            } else if ((SelectedResources.size() != 0) && (RefreshmentTimeComboBox.getValue() == null)) {

                NewBooking.Booking_Resources = SelectedResources.toString();

            } else {
                String[] StartTime = ((String) RefreshmentTimeComboBox.getValue()).split(":");
                LocalDateTime OldStartDateTime = BookingDateSelection.getValue().atTime(Integer.parseInt(StartTime[0]), Integer.parseInt(StartTime[1]));
                Date RefreshmentDateTime = Date.from(OldStartDateTime.toInstant(OffsetDateTime.now().getOffset()));

                NewBooking.Booking_RefreshmentsRequested = SelectedRefreshments.toString();
                NewBooking.Booking_RefreshmentsDeliveryTime = RefreshmentDateTime.getTime();
            }
        }

        DatabaseFunctions.createBooking(NewBooking);
        DatabaseFunctions.createBooking(NewCleaning);

        BookingExtrasPane.setVisible(false);
        MenuPane.setVisible(true);

        RefreshmentTimeComboBox.getSelectionModel().clearSelection();
        BookingRoomChoiceComboBox.getSelectionModel().clearSelection();
        BookingStartTimeComboBox.getSelectionModel().clearSelection();
        BookingEndTimeComboBox.getSelectionModel().clearSelection();
        BookingDateSelection.setValue(null);
        NewBooking = null;
        NewCleaning = null;
        ExtrasListView.getItems().clear();
    }

    @FXML
    private void OnContinue() {
        BookingStatusLabel.setText("");

        String SelectedRoom = (String) BookingRoomChoiceComboBox.getValue();
        if ((SelectedRoom != null) && (BookingDateSelection.getValue() != null) && (BookingStartTimeComboBox.getValue() != null) && (BookingEndTimeComboBox.getValue() != null) && (BookingStartTimeComboBox.getValue() != BookingEndTimeComboBox.getValue())) {

            String[] StartTime = ((String) BookingStartTimeComboBox.getValue()).split(":");
            LocalDateTime OldStartDateTime = BookingDateSelection.getValue().atTime(Integer.parseInt(StartTime[0]), Integer.parseInt(StartTime[1]));
            Date StartDateTime = Date.from(OldStartDateTime.toInstant(OffsetDateTime.now().getOffset()));

            String[] EndTime = ((String) BookingEndTimeComboBox.getValue()).split(":");
            LocalDateTime OldEndDateTime = BookingDateSelection.getValue().atTime(Integer.parseInt(EndTime[0]), Integer.parseInt(EndTime[1]));
            Date EndDateTime = Date.from(OldEndDateTime.toInstant(OffsetDateTime.now().getOffset()));

            if (EndDateTime.getTime() < StartDateTime.getTime()) {
                BookingStatusLabel.setText("Error: Invalid data entry");
                return;
            }

            ArrayList<Booking> RoomBookings = DatabaseFunctions.getRoomBookings((String) SelectedRoom);
            //RoomBookings = sortBookings(StartDateTime.getTime(), EndDateTime.getTime(), RoomBookings);

            boolean isactivebooking = false;

            for (long i = StartDateTime.getTime(); i < EndDateTime.getTime(); i = i + 1800000) {
                int activeBooking = isActiveBooking(RoomBookings, i);
                if (activeBooking != 0) {
                    isactivebooking = true;
                    break;
                }
            }

            if (!isactivebooking) {

                int cleanslotisactiveBooking = isActiveBooking(RoomBookings, EndDateTime.getTime());

                if (cleanslotisactiveBooking == 0) {
                    NewBooking = new Booking(-1, "MEETING", RoomBookingSystem.LoggedInUser.UserID, Integer.parseInt(SelectedRoom), StartDateTime.getTime(), EndDateTime.getTime() - 1, "[]", 0, "[]");
                    NewCleaning = new Booking(-1, "CLEANING", RoomBookingSystem.LoggedInUser.UserID, Integer.parseInt(SelectedRoom), EndDateTime.getTime(), EndDateTime.getTime() + 1800000 - 1, "[]", 0, "Cleaning resources");

                    ObservableList<String> RefreshmentTimes = FXCollections.observableArrayList();
                    for (long i = StartDateTime.getTime(); i <= EndDateTime.getTime() - 600000; i = i + 600000) {

                        Date TimeSlotDate = new Date(i);
                        DateFormat Format = new SimpleDateFormat("kk:mm");
                        String STRTimeSlot = Format.format(TimeSlotDate);
                        RefreshmentTimes.add(STRTimeSlot);
                    }
                    Collections.sort(RefreshmentTimes);
                    RefreshmentTimeComboBox.setItems(RefreshmentTimes);

                    BookingExtrasPane.setVisible(true);
                    CreateBookingPane.setVisible(false);

                    //RESET ALL VARIABLES AND DATA ENTRY FIELDS
                } else {
                    BookingStatusLabel.setText("Error: No cleaning slot available");
                }

            } else {
                BookingStatusLabel.setText("Error: Room in use at selected time");
            }

        } else {
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
