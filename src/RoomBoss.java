/*
    Liam Blair
    COS 480 - Database Management Systems
    September 24, 2021 - October 5, 2021
 */

import javax.swing.plaf.nimbus.State;
import java.sql.*;


public class RoomBoss {

    private String url = "jdbc:sqlite:db.sqlite";
    private Connection conn = null;

    public RoomBoss(){
        // Do I even need this??
    }

    public void connect(String username, String password) {
        // TODO username/password close conn
        // confused about connection with username and password

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTables() {
        // TODO might be done
        // not sure if ids are right
        String rooms = "CREATE TABLE IF NOT EXISTS Rooms (\n" +
                "id integer PRIMARY KEY,\n" +
                "building varchar(20) NOT NULL,\n" +
                "floor integer NOT NULL,\n" +
                "room integer NOT NULL,\n" +
                "directions varchar(100)\n" +
                ");";

        String facilities = "CREATE TABLE IF NOT EXISTS Facilities (\n" +
                "id integer PRIMARY KEY,\n" +
                "building varchar(20) NOT NULL,\n" +
                "room integer NOT NULL,\n" +
                "chair integer NOT NULL,\n" +
                "screens integer NOT NULL,\n" +
                "comments text\n" +
                ");";

        String reservations = "CREATE TABLE IF NOT EXISTS Reservations (\n" +
                "cnum integer PRIMARY KEY,\n" +
                "rstart text NOT NULL,\n" +
                "rend text NOT NULL,\n" +
                "building varchar(20) NOT NULL,\n" +
                "room integer NOT NULL,\n" +
                "name text NOT NULL\n" +
                ");";

        try {
            Statement statement = conn.createStatement();
            statement.execute(rooms);
            statement.execute(facilities);
            statement.execute(reservations);
            System.out.println("Tables Created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void destroyTables() {
        String dropRooms = "DROP TABLE IF EXISTS Rooms;";
        String dropFacilities = "DROP TABLE IF EXISTS Facilities;";
        String dropReservations = "DROP TABLE IF EXISTS Reservations;";

        try {
            Statement statement = conn.createStatement();
            statement.execute(dropRooms);
            statement.execute(dropFacilities);
            statement.execute(dropReservations);
            System.out.println("Tables Destroyed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int addRoom(String building, int floor, int room, String directions) {
        // TODO
        return 0;
    }

    public String findRoom(String s) {
        // TODO
        return null;
    }

    public String describeRoom(int identifier) {
        // TODO
        return null;
    }

    public int addFacilities(String building, int room, int chairs, int screens, String comments) {
        // TODO
        return 0;
    }

    public int addReservation(String start, String end, String building, int room, String name) {
        // TODO
        return 0;
    }

    public String describeReservation(int identifier) {
        // TODO
        return null;
    }

    public int makeReservation(String pattern, int length, String building, int room, String name) {
        // TODO
        return 0;
    }

    public void matchReservation(String pattern, String building, int room) {
        // TODO
    }

    public void getFreeRoom(String pattern, int length, int chairs, int screens) {
        // TODO
    }

}
